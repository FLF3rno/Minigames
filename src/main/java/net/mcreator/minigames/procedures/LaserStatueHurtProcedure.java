package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;

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
				int myId = (entity instanceof LaserStatueEntity _ls1) ? _ls1.getRoomID() : entity.getPersistentData().getIntOr("DataID", (int) entity.getPersistentData().getDoubleOr("DataID", 0));
				int otherId = (entityiterator instanceof LaserStatueEntity _ls2) ? _ls2.getRoomID() : entityiterator.getPersistentData().getIntOr("DataID", (int) entityiterator.getPersistentData().getDoubleOr("DataID", 0));
				if (myId > 0 && myId == otherId) {
					if (entityiterator instanceof LaserStatueEntity otherStatue && !(entityiterator == entity)) {
						if (otherStatue.isAlive()) {
							if (!entity.isAlive() || (entity instanceof LivingEntity _sourceLiv && _sourceLiv.getHealth() <= 0)) {
								otherStatue.removeEffect(MinigamesModMobEffects.BLESSED);
								if (world instanceof ServerLevel _serverLevel) {
									otherStatue.hurtServer(_serverLevel, _serverLevel.damageSources().generic(), Float.MAX_VALUE);
								}
								if (otherStatue.isAlive()) {
									otherStatue.die(otherStatue.damageSources().generic());
								}
							} else {
								otherStatue.setHealth((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1));
								otherStatue.removeEffect(MinigamesModMobEffects.BLESSED);
								otherStatue.getEntityData().set(LaserStatueEntity.ANIM, 1000);
								otherStatue.getEntityData().set(LaserStatueEntity.ANIM, 0);
								otherStatue.getEntityData().set(LaserStatueEntity.DATA_active, true);
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
}