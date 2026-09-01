package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.FlavioFightManager;

public class FlavioTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= 20) {
			if (!entity.getPersistentData().getBooleanOr("die", false)) {
				entity.getPersistentData().putBoolean("die", true);
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 100000, 50, false, false));
				net.mcreator.minigames.FlavioFightManager.nextPhase(world);
				if (entity instanceof FlavioEntity _ent4) {
					_ent4.getEntityData().set(FlavioEntity.ANIM, 1000);
					_ent4.getEntityData().set(FlavioEntity.ANIM, 4);
				}
				MinigamesMod.queueServerWork(24, () -> {
					ExplodeProcedure.execute(world, x, y, z, entity, true, true, 0, 1, 5, "normal");
					if (!entity.level().isClientSide())
						entity.discard();
				});
			}
		}
	}
}