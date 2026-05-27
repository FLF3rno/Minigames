package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.init.MinigamesModMobEffects;

public class ApplyGlowingProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
			_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100000000, 1, false, false));
			_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.ADVANCED_GLOWING, 100000000, 0, false, false));
		}
	}
}
