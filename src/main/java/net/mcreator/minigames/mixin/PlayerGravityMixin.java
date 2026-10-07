package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(Player.class)
public abstract class PlayerGravityMixin {
	@Shadow
	protected abstract boolean isStayingOnGroundSurface();

	@Inject(method = "canPlayerFitWithinBlocksAndEntitiesWhen", at = @At("HEAD"), cancellable = true)
	private void minigames$canPlayerFitRotated(Pose newPose, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		AABB box = player.getDimensions(newPose).makeBoundingBox(player.position());
		AABB localBox = box.move(player.position().scale(-1.0D));
		AABB worldBox = GravityOrientation.boxPlayerToWorld(localBox, gravity).move(player.position()).deflate(1.0E-7D);
		cir.setReturnValue(player.level().noCollision(player, worldBox));
	}

	@Inject(method = "maybeBackOffFromEdge", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityMaybeBackOffFromEdge(Vec3 movement, MoverType moverType, CallbackInfoReturnable<Vec3> cir) {
		Player player = (Player) (Object) this;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 playerMovement = GravityOrientation.vecWorldToPlayer(movement, gravity);

		if (!player.getAbilities().flying && (moverType == MoverType.SELF || moverType == MoverType.PLAYER) && this.isStayingOnGroundSurface()) {
			double d = playerMovement.x;
			double e = playerMovement.z;
			double stepHeight = (double) player.maxUpStep();

			while (d != 0.0D && player.level().noCollision(player, player.getBoundingBox().move(GravityOrientation.vecPlayerToWorld(d, -stepHeight, 0.0D, gravity)))) {
				if (d < 0.05D && d >= -0.05D) {
					d = 0.0D;
				} else if (d > 0.0D) {
					d -= 0.05D;
				} else {
					d += 0.05D;
				}
			}

			while (e != 0.0D && player.level().noCollision(player, player.getBoundingBox().move(GravityOrientation.vecPlayerToWorld(0.0D, -stepHeight, e, gravity)))) {
				if (e < 0.05D && e >= -0.05D) {
					e = 0.0D;
				} else if (e > 0.0D) {
					e -= 0.05D;
				} else {
					e += 0.05D;
				}
			}

			while (d != 0.0D && e != 0.0D && player.level().noCollision(player, player.getBoundingBox().move(GravityOrientation.vecPlayerToWorld(d, -stepHeight, e, gravity)))) {
				if (d < 0.05D && d >= -0.05D) {
					d = 0.0D;
				} else if (d > 0.0D) {
					d -= 0.05D;
				} else {
					d += 0.05D;
				}

				if (e < 0.05D && e >= -0.05D) {
					e = 0.0D;
				} else if (e > 0.0D) {
					e -= 0.05D;
				} else {
					e += 0.05D;
				}
			}

			cir.setReturnValue(GravityOrientation.vecPlayerToWorld(d, playerMovement.y, e, gravity));
		} else {
			cir.setReturnValue(movement);
		}
	}
}
