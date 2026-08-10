package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.minigames.procedures.BlessedEffectStartedProcedure;

public class BlessedMobEffect extends MobEffect {
	public BlessedMobEffect() {
		super(MobEffectCategory.NEUTRAL, -5966861);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		BlessedEffectStartedProcedure.execute(entity);
	}
}