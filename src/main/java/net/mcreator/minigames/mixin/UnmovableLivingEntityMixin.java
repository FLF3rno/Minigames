package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.UnmovableEntityHandler;

@Mixin(LivingEntity.class)
public abstract class UnmovableLivingEntityMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void minigames$onLivingTickHead(CallbackInfo ci) {
		UnmovableEntityHandler.pushTickingEntity((Entity) (Object) this);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void minigames$onLivingTickReturn(CallbackInfo ci) {
		UnmovableEntityHandler.popTickingEntity((Entity) (Object) this);
	}
}
