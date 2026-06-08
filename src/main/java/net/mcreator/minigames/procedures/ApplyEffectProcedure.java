package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.init.MinigamesModMobEffects;

public class ApplyEffectProcedure {
	public static void execute(Entity target, boolean hide, double level, double ticks, String effect) {
		if (target == null || effect == null)
			return;
		if ((effect).equals("minigames:stunned")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.STUNNED, (int) ticks, (int) (level - 1), false, (!hide)));
		} else if ((effect).equals("minigames:blessed")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLESSED, (int) ticks, (int) (level - 1), false, (!hide)));
		} else if ((effect).equals("strength")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, (int) ticks, (int) (level - 1), false, (!hide)));
		}
	}
}