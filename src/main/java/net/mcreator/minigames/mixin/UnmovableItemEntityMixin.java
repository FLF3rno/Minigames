package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.UnmovableEntityHandler;

@Mixin(ItemEntity.class)
public abstract class UnmovableItemEntityMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void minigames$onTickHead(CallbackInfo ci) {
		UnmovableEntityHandler.setSelfLocomotion((Entity) (Object) this, true);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void minigames$onTickReturn(CallbackInfo ci) {
		UnmovableEntityHandler.setSelfLocomotion((Entity) (Object) this, false);
	}
}
