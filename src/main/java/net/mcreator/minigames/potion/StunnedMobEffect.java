package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.procedures.StunnedOnEffectActiveTickProcedure;
import net.mcreator.minigames.procedures.StunnedEffectStartedappliedProcedure;
import net.mcreator.minigames.procedures.StunnedEffectExpiresProcedure;

public class StunnedMobEffect extends MobEffect {
	public StunnedMobEffect() {
		super(MobEffectCategory.NEUTRAL, -256);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		StunnedEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
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

	@Override
	public void onMobRemoved(ServerLevel level, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
		if (reason == Entity.RemovalReason.KILLED) {
			StunnedEffectExpiresProcedure.execute(level, entity.getX(), entity.getY(), entity.getZ(), entity);
		}
	}
}