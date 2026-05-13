package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import net.mcreator.minigames.init.MinigamesModMobEffects;

@Mixin(TargetingConditions.class)
public class TargetingConditionsMixin {
	@Inject(method = "test(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void minigames$blockBlessedTargets(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		if (attacker instanceof Mob && target != null && target.hasEffect(MinigamesModMobEffects.BLESSED)) {
			cir.setReturnValue(false);
		}
	}
}
