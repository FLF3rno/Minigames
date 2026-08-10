package net.mcreator.minigames.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.procedures.DecayTickProcedure;

public class DecayMobEffect extends MobEffect {
	public DecayMobEffect() {
		super(MobEffectCategory.HARMFUL, -6736897);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		DecayTickProcedure.execute(level, entity, amplifier);
		return super.applyEffectTick(level, entity, amplifier);
	}
}