package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.CustomFireHelper;

public class SetOnFireProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity, double damage, double ticks, String color) {
		if (entity == null)
			return;
		CustomFireHelper.setOnFire(world, entity, sourceentity, damage, ticks, color);
	}
}