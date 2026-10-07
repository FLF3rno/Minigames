package net.mcreator.minigames.mixin;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(Camera.class)
public abstract class CameraRotationGravityMixin {
	@Shadow @Final private static Vector3f FORWARDS;
	@Shadow @Final private static Vector3f UP;
	@Shadow @Final private static Vector3f LEFT;

	@Shadow private Entity entity;
	@Shadow private Quaternionf rotation;
	@Shadow private Vector3f forwards;
	@Shadow private Vector3f up;
	@Shadow private Vector3f left;

	@Shadow protected abstract void setPosition(double x, double y, double z);

	@Shadow private float eyeHeight;
	@Shadow private float eyeHeightOld;
	@Shadow private net.minecraft.world.level.Level level;
	@Shadow private Vec3 position;

	@Inject(method = "alignWithEntity", at = @At("HEAD"))
	private void minigames$updateGravityTransition(float partialTicks, CallbackInfo ci) {
		if (this.entity instanceof Player player) {
			net.mcreator.minigames.gravity.client.GravityCameraTransition.update(player, partialTicks);
		}
	}

	@Inject(
		method = "alignWithEntity",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Camera;setPosition(DDD)V",
			shift = At.Shift.AFTER
		)
	)
	private void minigames$afterSetPosition(float partialTicks, CallbackInfo ci) {
		if (!(this.entity instanceof Player player)) return;
		if (!net.mcreator.minigames.gravity.client.GravityCameraTransition.isNonDefault()) return;

		float currentEyeHeight = Mth.lerp(partialTicks, this.eyeHeightOld, this.eyeHeight);
		double lerpedX = Mth.lerp(partialTicks, player.xo, player.getX());
		double lerpedY = Mth.lerp(partialTicks, player.yo, player.getY());
		double lerpedZ = Mth.lerp(partialTicks, player.zo, player.getZ());
		Vector3f eyeOffset = new Vector3f(0.0F, currentEyeHeight, 0.0F);
		eyeOffset.rotate(net.mcreator.minigames.gravity.client.GravityCameraTransition.getCurrentRotation());
		this.setPosition(lerpedX + eyeOffset.x, lerpedY + eyeOffset.y, lerpedZ + eyeOffset.z);
	}

	@Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityMaxZoom(float cameraDist, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Float> cir) {
		if (!(this.entity instanceof Player player)) return;
		if (!net.mcreator.minigames.gravity.client.GravityCameraTransition.isNonDefault()) return;
		if (this.level == null) return;

		for (int i = 0; i < 8; i++) {
			float offsetX = (i & 1) * 2 - 1;
			float offsetY = (i >> 1 & 1) * 2 - 1;
			float offsetZ = (i >> 2 & 1) * 2 - 1;
			Vec3 from = this.position.add(offsetX * 0.1F, offsetY * 0.1F, offsetZ * 0.1F);
			net.minecraft.core.BlockPos testPos = net.minecraft.core.BlockPos.containing(from);
			if (this.level.getBlockState(testPos).isViewBlocking(this.level, testPos)) {
				cir.setReturnValue(0.0F);
				return;
			}
			Vec3 to = from.add(new Vec3(this.forwards).scale(-cameraDist));
			net.minecraft.world.phys.HitResult hitResult = this.level.clip(new net.minecraft.world.level.ClipContext(from, to, net.minecraft.world.level.ClipContext.Block.VISUAL, net.minecraft.world.level.ClipContext.Fluid.NONE, this.entity));
			if (hitResult.getType() != net.minecraft.world.phys.HitResult.Type.MISS) {
				float distSq = (float) hitResult.getLocation().distanceToSqr(this.position);
				if (distSq < Mth.square(cameraDist)) {
					cameraDist = Mth.sqrt(distSq);
				}
			}
		}
		cir.setReturnValue(cameraDist);
	}

	@Inject(method = "setRotation(FFF)V", at = @At("TAIL"))
	private void minigames$applyGravityCameraRotation(float yRot, float xRot, float roll, CallbackInfo ci) {
		if (!(this.entity instanceof Player player)) return;
		if (!net.mcreator.minigames.gravity.client.GravityCameraTransition.isNonDefault()) return;

		Quaternionf gravityRot = new Quaternionf(net.mcreator.minigames.gravity.client.GravityCameraTransition.getCurrentRotation());
		gravityRot.mul(this.rotation);
		this.rotation.set(gravityRot);

		FORWARDS.rotate(this.rotation, this.forwards);
		UP.rotate(this.rotation, this.up);
		LEFT.rotate(this.rotation, this.left);
	}
}
