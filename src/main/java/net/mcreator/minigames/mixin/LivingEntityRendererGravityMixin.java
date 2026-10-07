package net.mcreator.minigames.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;
import net.mcreator.minigames.gravity.GravityRenderState;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererGravityMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void minigames$extractGravityState(T entity, S state, float partialTick, CallbackInfo ci) {
		if (state instanceof GravityRenderState grs && entity instanceof Player player) {
			grs.minigames_setGravity(GravityAccess.getGravity(player));
		}
	}

	@Inject(
		method = "setupRotations",
		at = @At("HEAD")
	)
	private void minigames$rotateModelWithGravity(S state, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
		if (state instanceof GravityRenderState grs) {
			Direction gravity = grs.minigames_getGravity();
			if (gravity != Direction.DOWN) {
				poseStack.mulPose(GravityOrientation.getCameraRotation(gravity));
			}
		}
	}
}
