package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityTravelHandler;

import org.spongepowered.asm.mixin.Shadow;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTravelGravityMixin {
	@Shadow
	protected abstract void updateWalkAnimation(float distance);

	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	private void minigames$customGravityTravel(Vec3 travelVector, CallbackInfo ci) {
		if (!((Object) this instanceof Player player)) return;
		Direction direction = GravityAccess.getGravity(player);
		if (direction == Direction.DOWN) return;

		GravityTravelHandler.travel(player, travelVector, direction);
		ci.cancel();
	}

	@Inject(method = "calculateEntityAnimation", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityCalculateEntityAnimation(boolean useY, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (!(entity instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 deltaPos = new Vec3(player.getX() - player.xo, player.getY() - player.yo, player.getZ() - player.zo);
		Vec3 perp = GravityOrientation.getPerpendicularComponent(deltaPos, gravity);
		float distance = (float) perp.length();

		if (!player.isPassenger() && player.isAlive()) {
			this.updateWalkAnimation(distance);
		} else {
			player.walkAnimation.stop();
		}
		ci.cancel();
	}

	@org.spongepowered.asm.mixin.injection.ModifyArg(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;tickHeadTurn(F)V"
		),
		index = 0
	)
	private float minigames$gravityCorrectYBodyRot(float yBodyRotT) {
		if (!((Object) this instanceof Player player)) return yBodyRotT;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return yBodyRotT;

		Vec3 worldDelta = player.position().subtract(player.xo, player.yo, player.zo);
		Vec3 localDelta = GravityOrientation.vecWorldToPlayer(worldDelta, gravity);
		double localXd = localDelta.x;
		double localZd = localDelta.z;
		float localSideDist = (float) (localXd * localXd + localZd * localZd);

		if (localSideDist > 0.0025000002F) {
			float walkDirection = (float) net.minecraft.util.Mth.atan2(localZd, localXd) * (180.0F / (float) Math.PI) - 90.0F;
			float diffBetweenDirectionAndFacing = net.minecraft.util.Mth.abs(net.minecraft.util.Mth.wrapDegrees(player.getYRot()) - walkDirection);
			if (95.0F < diffBetweenDirectionAndFacing && diffBetweenDirectionAndFacing < 265.0F) {
				yBodyRotT = walkDirection - 180.0F;
			} else {
				yBodyRotT = walkDirection;
			}
		} else {
			yBodyRotT = player.yBodyRot;
		}

		if (player.attackAnim > 0.0F) {
			yBodyRotT = player.getYRot();
		}
		return yBodyRotT;
	}
}
