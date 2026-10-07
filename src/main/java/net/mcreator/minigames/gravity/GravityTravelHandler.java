package net.mcreator.minigames.gravity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class GravityTravelHandler {
	private GravityTravelHandler() {}

	public static void travel(Player player, Vec3 travelVector, Direction gravity) {
		if (player.isSwimming() || player.isInWater() || player.isInLava()) {
			return;
		}

		Vec3 down = GravityOrientation.getDownVector(gravity);
		double prevDown = player.getDeltaMovement().dot(down);
		boolean onGround = player.onGround() && prevDown >= -0.01;

		BlockPos groundPos = GravitySupport.getGroundBlockPos(player, gravity);
		float blockFriction = onGround ? player.level().getBlockState(groundPos).getBlock().getFriction() : 1.0F;
		float friction = blockFriction * 0.91F;

		if (onGround) {
			player.fallDistance = 0.0F;
		}

		float speed = onGround ? player.getSpeed() * (0.21600002F / (blockFriction * blockFriction * blockFriction)) : 0.02F;

		float f = (float) travelVector.lengthSqr();
		Vec3 inputWorld = Vec3.ZERO;
		if (f > 1.0E-7F) {
			Vec3 norm = f > 1.0F ? travelVector.normalize() : travelVector;

			Vec3 look = player.getViewVector(1.0F);
			Vec3 forward = look.subtract(down.scale(look.dot(down)));
			if (forward.lengthSqr() < 1.0E-6) {
				Vec3 fallback = player.getForward();
				forward = fallback.subtract(down.scale(fallback.dot(down)));
			}
			if (forward.lengthSqr() > 1.0E-6) {
				forward = forward.normalize();
			} else {
				forward = GravityOrientation.getUpVector(gravity);
			}

			Vec3 left = forward.cross(down).normalize();
			Vec3 wishDir = forward.scale(norm.z).add(left.scale(norm.x));
			inputWorld = wishDir.scale(speed);
		}

		Vec3 currentVel = player.getDeltaMovement().add(inputWorld);
		player.setDeltaMovement(currentVel);

		Vec3 startPos = player.position();
		player.move(MoverType.SELF, currentVel);
		Vec3 actualMoved = player.position().subtract(startPos);

		double deltaDown = currentVel.dot(down);
		double movedDown = actualMoved.dot(down);

		boolean landed = deltaDown > 0.001 && movedDown < deltaDown - 1.0E-4;
		boolean supported = deltaDown >= -0.001 && GravitySupport.isSupportedInGravity(player, gravity);
		onGround = landed || supported;
		player.setOnGround(onGround);

		Vec3 localVel = GravityOrientation.vecWorldToPlayer(currentVel, gravity);
		Vec3 localMoved = GravityOrientation.vecWorldToPlayer(actualMoved, gravity);

		double localX = Math.abs(localMoved.x) < Math.abs(localVel.x) - 1.0E-4 ? 0.0 : localVel.x;
		double localZ = Math.abs(localMoved.z) < Math.abs(localVel.z) - 1.0E-4 ? 0.0 : localVel.z;
		Vec3 finalPerp = GravityOrientation.vecPlayerToWorld(localX, 0, localZ, gravity);

		Vec3 finalParallel;
		if (onGround) {
			finalPerp = finalPerp.scale(friction);
			finalParallel = down.scale(0.08 * 0.98);
			player.fallDistance = 0.0F;
		} else {
			finalPerp = finalPerp.scale(0.91);
			double parallelSpeed = deltaDown;
			if (deltaDown < -0.001 && movedDown > deltaDown + 1.0E-4) {
				parallelSpeed = 0.0;
			}
			parallelSpeed = (parallelSpeed + 0.08) * 0.98;
			if (parallelSpeed > 3.92) {
				parallelSpeed = 3.92;
			}
			finalParallel = down.scale(parallelSpeed);
		}

		player.setDeltaMovement(finalPerp.add(finalParallel));
	}
}
