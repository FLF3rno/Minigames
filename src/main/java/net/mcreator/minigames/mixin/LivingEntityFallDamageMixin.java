package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFallDamageMixin {
	@Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
	private void minigames$cancelVanillaFallDamage(double distance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = player.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity;
		if (gravity != Direction.DOWN) {
			cir.setReturnValue(false);
		}
	}
}
