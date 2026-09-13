package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.entity.LaserStatueEntity;

import java.util.Comparator;

public class LaserStatueHurtProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double amount) {
		if (entity == null)
			return;
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(60 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entity.getPersistentData().getDoubleOr("DataID", 0) == entityiterator.getPersistentData().getDoubleOr("DataID", 0)) {
					if (entityiterator instanceof LaserStatueEntity && !(entityiterator == entity)) {
						if (entityiterator.isAlive()) {
							if (entityiterator instanceof LivingEntity _entity)
								_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - amount));
							if (entityiterator instanceof LivingEntity _entity)
								_entity.removeEffect(MinigamesModMobEffects.BLESSED);
							if (entityiterator instanceof LaserStatueEntity _ent8) {
								_ent8.getEntityData().set(LaserStatueEntity.ANIM, 1000);
								_ent8.getEntityData().set(LaserStatueEntity.ANIM, 0);
							}
							if (entityiterator instanceof LaserStatueEntity _datEntSetL)
								_datEntSetL.getEntityData().set(LaserStatueEntity.DATA_active, true);
							if (entity.isAlive()) {
								if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
									_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLESSED, 100000, 1, false, false));
								if (entity instanceof LaserStatueEntity _ent12) {
									_ent12.getEntityData().set(LaserStatueEntity.ANIM, 1000);
									_ent12.getEntityData().set(LaserStatueEntity.ANIM, 1);
								}
								if (entity instanceof LaserStatueEntity _datEntSetL)
									_datEntSetL.getEntityData().set(LaserStatueEntity.DATA_active, false);
							}
						}
					}
				}
			}
		}
	}
}