package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;


import java.util.Comparator;

public class GoldenEffectExpiresProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(world, x, y, z, entity, 0);
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double amplifier) {
		if (entity == null)
			return;

		if (entity instanceof net.minecraft.world.entity.Mob mob && entity.isAlive() && !entity.isRemoved()) {
			mob.setNoAi(false);
		}
	}
}




