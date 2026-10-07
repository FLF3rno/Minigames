package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.StunnedEffectEntity;

import java.util.Comparator;

public class StunnedEffectExpiresProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(world, x, y, z, entity, 0);
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double amplifier) {
		if (entity == null)
			return;

		if (entity instanceof net.minecraft.world.entity.Mob mob && entity.isAlive() && !entity.isRemoved()) {
			mob.setNoAi(false);
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(StunnedEffectEntity.class, new AABB(_center, _center).inflate(5.0d))) {
				if (entityiterator instanceof StunnedEffectEntity stunned && stunned.getParentId() == entity.getId()) {
					if (!entityiterator.level().isClientSide())
						entityiterator.discard();
				}
			}
		}
	}
}




