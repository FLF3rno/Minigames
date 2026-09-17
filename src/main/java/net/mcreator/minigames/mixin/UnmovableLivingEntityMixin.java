package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.UnmovableEntityHandler;

@Mixin(LivingEntity.class)
public abstract class UnmovableLivingEntityMixin {
	@Inject(method = "travel", at = @At("HEAD"))
	private void minigames$onTravelHead(Vec3 travelVector, CallbackInfo ci) {
		UnmovableEntityHandler.setSelfLocomotion((Entity) (Object) this, true);
	}

	@Inject(method = "travel", at = @At("RETURN"))
	private void minigames$onTravelReturn(Vec3 travelVector, CallbackInfo ci) {
		UnmovableEntityHandler.setSelfLocomotion((Entity) (Object) this, false);
	}
}
