package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerGravityMixin {
	@Shadow
	protected abstract boolean isSprintingPossible(boolean allowedInShallowWater);

	@Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
	private void minigames$cancelMoveTowardsClosestSpace(double x, double z, CallbackInfo ci) {
		LocalPlayer player = (LocalPlayer) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity != Direction.DOWN) {
			ci.cancel();
		}
	}

	@Inject(method = "shouldStopRunSprinting", at = @At("HEAD"), cancellable = true)
	private void minigames$shouldStopRunSprinting(org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
		LocalPlayer player = (LocalPlayer) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		cir.setReturnValue(
			!this.isSprintingPossible(player.getAbilities().flying)
			|| !player.input.hasForwardImpulse()
			|| (player.horizontalCollision && !player.minorHorizontalCollision)
		);
	}

	@Inject(method = "isHorizontalCollisionMinor", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityIsHorizontalCollisionMinor(net.minecraft.world.phys.Vec3 movement, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
		LocalPlayer player = (LocalPlayer) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		net.minecraft.world.phys.Vec3 localMovement = GravityOrientation.vecWorldToPlayer(movement, gravity);
		double desiredDistSq = player.xxa * player.xxa + player.zza * player.zza;
		double movedDistSq = localMovement.x * localMovement.x + localMovement.z * localMovement.z;
		if (desiredDistSq >= 1.0E-5 && movedDistSq >= 1.0E-5) {
			double dotProduct = player.xxa * localMovement.x + player.zza * localMovement.z;
			double angle = Math.acos(dotProduct / Math.sqrt(desiredDistSq * movedDistSq));
			cir.setReturnValue(angle < 0.13962634F);
		} else {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "move", at = @At("TAIL"))
	private void minigames$gravityAddWalkedDistance(net.minecraft.world.entity.MoverType moverType, net.minecraft.world.phys.Vec3 delta, CallbackInfo ci) {
		LocalPlayer player = (LocalPlayer) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		net.minecraft.world.phys.Vec3 actualMoved = player.position().subtract(player.xo, player.yo, player.zo);
		net.minecraft.world.phys.Vec3 perp = GravityOrientation.getPerpendicularComponent(actualMoved, gravity);
		float perpDist = (float) perp.length();

		float deltaX = (float) (player.getX() - player.xo);
		float deltaZ = (float) (player.getZ() - player.zo);
		float vanillaAdded = net.minecraft.util.Mth.length(deltaX, deltaZ) * 0.6F;
		float diff = (perpDist * 0.6F) - vanillaAdded;
		if (diff > 0.0F) {
			player.avatarState().addWalkDistance(diff);
		}
	}
}
