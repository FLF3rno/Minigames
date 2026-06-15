package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityTargetMixin {

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void blockBlessedAttacks(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target != null && target.hasEffect(MinigamesModMobEffects.BLESSED)) {
            cir.setReturnValue(false);
        }
    }
}