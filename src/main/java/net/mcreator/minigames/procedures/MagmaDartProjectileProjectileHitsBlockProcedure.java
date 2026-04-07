package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.MagmaHitboxEntity;

import java.util.Comparator;

public class MagmaDartProjectileProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;
		if (world instanceof ServerLevel _level) {
			Entity entityToSpawn = MinigamesModEntities.MAGMA_HITBOX.get().spawn(_level, BlockPos.containing(x, y + 1, z), EntitySpawnReason.MOB_SUMMONED);
			if (entityToSpawn != null) {
				entityToSpawn.setYRot(immediatesourceentity.getYRot() + 180);
				entityToSpawn.setYBodyRot(immediatesourceentity.getYRot() + 180);
				entityToSpawn.setYHeadRot(immediatesourceentity.getYRot() + 180);
				entityToSpawn.setDeltaMovement(0, 0, 0);
			}
		}
		{
			final Vec3 _center = new Vec3(x, (y + 1), z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof MagmaHitboxEntity) {
					if (entityiterator instanceof MagmaHitboxEntity _datEntSetI)
						_datEntSetI.getEntityData().set(MagmaHitboxEntity.DATA_yaw, (int) (immediatesourceentity.getYRot() * (-1)));
				}
			}
		}
	}
}