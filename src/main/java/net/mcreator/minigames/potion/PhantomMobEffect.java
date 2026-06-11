package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.minigames.procedures.PhantomEffectAppliedProcedure;

public class PhantomMobEffect extends MobEffect {
	public PhantomMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -11974965);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		PhantomEffectAppliedProcedure.execute(entity);
	}
}