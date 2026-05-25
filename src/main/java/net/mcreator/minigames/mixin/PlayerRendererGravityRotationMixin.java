package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.MinigamesModPlayerAnimationAPI;
import net.mcreator.minigames.gravity.GravityAccess;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererGravityRotationMixin {
	@Inject(method = "setupRotations", at = @At("HEAD"))
	private void minigames$applyGravityRotation(PlayerRenderState renderState, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
		Player player = (Player) renderState.getRenderData(MinigamesModPlayerAnimationAPI.ClientAttachments.PLAYER);
		if (player == null) return;
		Direction gravity = GravityAccess.getGravity(player);
		switch (gravity) {
			case UP -> { poseStack.mulPose(Axis.XP.rotationDegrees(180.0F)); poseStack.translate(0.0D, -1.8D, 0.0D); }
			case NORTH -> { poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F)); }
			case SOUTH -> { poseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); }
			case EAST -> { poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F)); }
			case WEST -> { poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F)); }
			default -> {
			}
		}
	}
}
