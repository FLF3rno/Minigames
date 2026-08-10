package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.procedures.BleedTickProcedure;

public class BleedMobEffect extends MobEffect {
	public BleedMobEffect() {
		super(MobEffectCategory.HARMFUL, -4973283);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		BleedTickProcedure.execute(level, entity);
		return super.applyEffectTick(level, entity, amplifier);
	}
}