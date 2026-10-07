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
import net.mcreator.minigames.gravity.GravityMath;

@Mixin(LivingEntity.class)
public abstract class LivingEntityJumpGravityMixin {
	@org.spongepowered.asm.mixin.Shadow
	protected abstract float getJumpPower();

	@Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
	private void minigames$jumpAlongGravityUp(CallbackInfo ci) {
		if (!((Object) this instanceof LivingEntity living)) return;
		if (net.mcreator.minigames.entity.ChurchSharpenerEntity.isVictimPinned(living)) {
			ci.cancel();
			return;
		}
		if (!(living instanceof Player)) return;
		Direction gravity = GravityAccess.getGravity(living);
		if (gravity == Direction.DOWN) return;

		float jumpPower = this.getJumpPower();
		Vec3 up = net.mcreator.minigames.gravity.GravityOrientation.getUpVector(gravity);
		Vec3 down = net.mcreator.minigames.gravity.GravityOrientation.getDownVector(gravity);
		Vec3 vel = living.getDeltaMovement();
		Vec3 perpendicular = vel.subtract(down.scale(vel.dot(down)));
		Vec3 jumpVel = perpendicular.add(up.scale(jumpPower));

		if (living.isSprinting()) {
			Vec3 look = living.getViewVector(1.0F);
			Vec3 forward = look.subtract(down.scale(look.dot(down)));
			if (forward.lengthSqr() < 1.0E-6) {
				Vec3 fallback = living.getForward();
				forward = fallback.subtract(down.scale(fallback.dot(down)));
			}
			if (forward.lengthSqr() > 1.0E-6) {
				forward = forward.normalize();
				jumpVel = jumpVel.add(forward.scale(0.2));
			}
		}

		living.setDeltaMovement(jumpVel);
		living.setOnGround(false);
		ci.cancel();
	}
}

