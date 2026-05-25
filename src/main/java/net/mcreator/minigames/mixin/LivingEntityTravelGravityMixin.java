package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.minigames.gravity.GravityAccess;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTravelGravityMixin {
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getGravity()D"))
	private double minigames$customGravityStrength(LivingEntity self) {
		Direction direction = GravityAccess.getGravity(self);
		return direction == Direction.DOWN ? self.getGravity() : 0.0D;
	}
}
