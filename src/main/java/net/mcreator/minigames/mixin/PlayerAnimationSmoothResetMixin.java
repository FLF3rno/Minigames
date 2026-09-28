package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;

import net.mcreator.minigames.MinigamesModPlayerAnimationAPI;

import java.util.WeakHashMap;
import java.util.Map;
import java.util.HashMap;

@Mixin(value = PlayerModel.class, priority = 1100)
public abstract class PlayerAnimationSmoothResetMixin {
	private static final Map<Player, PlayerAnimData> PLAYER_DATA = new WeakHashMap<>();
	private static final String[] BONE_NAMES = {"head", "torso", "right_arm", "left_arm", "right_leg", "left_leg"};

	private static class BonePose {
		float xRot;
		float yRot;
		float zRot;
		float x;
		float y;
		float z;
		float xScale = 1.0f;
		float yScale = 1.0f;
		float zScale = 1.0f;

		void set(ModelPart part) {
			this.xRot = part.xRot;
			this.yRot = part.yRot;
			this.zRot = part.zRot;
			this.x = part.x;
			this.y = part.y;
			this.z = part.z;
			this.xScale = part.xScale;
			this.yScale = part.yScale;
			this.zScale = part.zScale;
		}

		void set(BonePose other) {
			this.xRot = other.xRot;
			this.yRot = other.yRot;
			this.zRot = other.zRot;
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
			this.xScale = other.xScale;
			this.yScale = other.yScale;
			this.zScale = other.zScale;
		}
	}

	private static class BoneResetState {
		final BonePose fromPose = new BonePose();
		final BonePose lastRenderedPose = new BonePose();
		boolean hasLastRendered = false;
		boolean wasAnimated = false;
		boolean isBlending = false;
		long blendStartTime = 0;
		long blendDuration = 100;

		void startBlend(long now, long duration) {
			fromPose.set(lastRenderedPose);
			blendStartTime = now;
			blendDuration = Math.max(duration, 10);
			isBlending = true;
		}
	}

	private static class PlayerAnimData {
		String lastAnimationName = "";
		boolean pendingOverride = false;
		boolean pendingReset = false;
		final Map<String, BoneResetState> bones = new HashMap<>();

		BoneResetState getBone(String name) {
			return bones.computeIfAbsent(name, k -> new BoneResetState());
		}
	}

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
	public void onSetupAnimHead(AvatarRenderState renderState, CallbackInfo ci) {
		Player player = (Player) renderState.getRenderData(MinigamesModPlayerAnimationAPI.ClientAttachments.PLAYER);
		if (player == null) {
			return;
		}
		CompoundTag data = player.getPersistentData();
		PlayerAnimData animData = PLAYER_DATA.computeIfAbsent(player, p -> new PlayerAnimData());
		animData.pendingOverride = data.getBooleanOr("OverrideCurrentAnimation", false);
		animData.pendingReset = data.getBooleanOr("ResetPlayerAnimation", false);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
	public void onSetupAnimTail(AvatarRenderState renderState, CallbackInfo ci) {
		if (renderState.ageInTicks <= 0) {
			return;
		}
		Player player = (Player) renderState.getRenderData(MinigamesModPlayerAnimationAPI.ClientAttachments.PLAYER);
		if (player == null) {
			return;
		}
		PlayerModel model = (PlayerModel) (Object) this;
		CompoundTag data = player.getPersistentData();
		PlayerAnimData animData = PLAYER_DATA.computeIfAbsent(player, p -> new PlayerAnimData());

		String currentAnimation = data.getStringOr("PlayerCurrentAnimation", "");
		Minecraft mc = Minecraft.getInstance();
		boolean firstPersonBlocked = !data.getBooleanOr("FirstPersonAnimation", false)
				&& mc.options.getCameraType().isFirstPerson()
				&& player == mc.player
				&& (mc.screen == null || mc.screen instanceof ChatScreen);

		MinigamesModPlayerAnimationAPI.PlayerAnimation activeAnim = MinigamesModPlayerAnimationAPI.active_animations.get(player);
		boolean animChanged = !currentAnimation.equals(animData.lastAnimationName)
				|| animData.pendingOverride
				|| animData.pendingReset;

		long now = System.currentTimeMillis();
		long defaultDuration = (long) data.getIntOr("AnimationBlendDuration", 100);

		for (String boneName : BONE_NAMES) {
			ModelPart part = minigames$reset$getModelPart(model, boneName);
			if (part == null) {
				continue;
			}
			BoneResetState boneState = animData.getBone(boneName);

			boolean isBoneAnimatedNow = false;
			if (activeAnim != null && !currentAnimation.isEmpty() && !firstPersonBlocked) {
				if (activeAnim.bones.containsKey(boneName) || (boneName.equals("torso") && activeAnim.bones.containsKey("body"))) {
					isBoneAnimatedNow = true;
				}
			}

			if (boneState.wasAnimated && !isBoneAnimatedNow) {
				boneState.startBlend(now, defaultDuration);
				boneState.wasAnimated = false;
			} else if (isBoneAnimatedNow && animChanged && boneState.hasLastRendered) {
				boneState.startBlend(now, defaultDuration);
				boneState.wasAnimated = true;
			} else if (isBoneAnimatedNow) {
				boneState.wasAnimated = true;
			}

			if (boneState.isBlending) {
				long elapsed = now - boneState.blendStartTime;
				if (elapsed >= boneState.blendDuration) {
					boneState.isBlending = false;
				} else {
					float alpha = Math.clamp((float) elapsed / (float) boneState.blendDuration, 0.0f, 1.0f);
					float smoothAlpha = alpha * alpha * (3.0f - 2.0f * alpha);

					part.xRot = minigames$reset$lerpAngle(smoothAlpha, boneState.fromPose.xRot, part.xRot);
					part.yRot = minigames$reset$lerpAngle(smoothAlpha, boneState.fromPose.yRot, part.yRot);
					part.zRot = minigames$reset$lerpAngle(smoothAlpha, boneState.fromPose.zRot, part.zRot);

					part.x = Mth.lerp(smoothAlpha, boneState.fromPose.x, part.x);
					part.y = Mth.lerp(smoothAlpha, boneState.fromPose.y, part.y);
					part.z = Mth.lerp(smoothAlpha, boneState.fromPose.z, part.z);

					part.xScale = Mth.lerp(smoothAlpha, boneState.fromPose.xScale, part.xScale);
					part.yScale = Mth.lerp(smoothAlpha, boneState.fromPose.yScale, part.yScale);
					part.zScale = Mth.lerp(smoothAlpha, boneState.fromPose.zScale, part.zScale);
				}
			}

			boneState.lastRenderedPose.set(part);
			boneState.hasLastRendered = true;
		}

		animData.lastAnimationName = currentAnimation;
		animData.pendingOverride = false;
		animData.pendingReset = false;
	}

	private static float minigames$reset$lerpAngle(float delta, float start, float end) {
		float twoPi = (float) (Math.PI * 2.0);
		float diff = (end - start) % twoPi;
		if (diff < -(float) Math.PI) {
			diff += twoPi;
		} else if (diff > (float) Math.PI) {
			diff -= twoPi;
		}
		return start + delta * diff;
	}

	private static ModelPart minigames$reset$getModelPart(PlayerModel model, String boneName) {
		switch (boneName) {
			case "torso":
			case "body":
				return model.body;
			case "head":
				return model.head;
			case "right_arm":
				return model.rightArm;
			case "left_arm":
				return model.leftArm;
			case "right_leg":
				return model.rightLeg;
			case "left_leg":
				return model.leftLeg;
			default:
				return null;
		}
	}
}
