package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.init.MinigamesModParticleTypes;
import net.mcreator.minigames.init.MinigamesModMobEffects;

public class BleedTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().y()) + Math.abs(entity.getDeltaMovement().z()) > 0.079) {
			{
				Entity _ent = entity;
				if (_ent.level() instanceof ServerLevel _serverLevel) {
					_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), (float) ((Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().y()) + Math.abs(entity.getDeltaMovement().z()))
							* ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.BLEED) ? _livEnt.getEffect(MinigamesModMobEffects.BLEED).getAmplifier() : 0) + 1) * 0.8));
				}
			}
			if (world instanceof ServerLevel _level) {
				_level.sendParticles(MinigamesModParticleTypes.BLOOD.get(), entity.getX(), entity.getY() + 0.5, entity.getZ(), 7, 0.25, 0.4, 0.25, 0.1);
			}
		}
	}
}