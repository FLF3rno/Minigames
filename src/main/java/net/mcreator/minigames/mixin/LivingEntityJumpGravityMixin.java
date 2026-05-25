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
	@Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
	private void minigames$jumpAlongGravityUp(CallbackInfo ci) {
		if (!((Object) this instanceof LivingEntity living)) return;
		if (!(living instanceof Player)) return;
		Direction gravity = GravityAccess.getGravity(living);
		if (gravity == Direction.DOWN) return;

		Vec3 jumpVec = GravityMath.up(gravity).scale(0.42D);
		Vec3 motion = living.getDeltaMovement();
		living.setDeltaMovement(motion.add(jumpVec));
		living.hasImpulse = true;
		ci.cancel();
	}
}
