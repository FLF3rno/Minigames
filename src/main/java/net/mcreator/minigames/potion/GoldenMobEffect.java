package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.procedures.StunnedOnEffectActiveTickProcedure;
import net.mcreator.minigames.procedures.GoldenEffectStartedappliedProcedure;

public class GoldenMobEffect extends MobEffect {
	public GoldenMobEffect() {
		super(MobEffectCategory.HARMFUL, -13312);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		GoldenEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		StunnedOnEffectActiveTickProcedure.execute(entity);
		return super.applyEffectTick(level, entity, amplifier);
	}
}