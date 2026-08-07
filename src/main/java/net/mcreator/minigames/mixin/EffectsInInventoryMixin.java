package net.mcreator.minigames.mixin;

import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.util.EffectUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;


@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {


    @ModifyVariable(
            method = "extractRenderState",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private Collection<MobEffectInstance> hideXaeroEffects(
            Collection<MobEffectInstance> effects
    ) {

        return effects.stream()
                .filter(effect -> !EffectUtils.isHiddenEffect(effect))
                .toList();
    }
}