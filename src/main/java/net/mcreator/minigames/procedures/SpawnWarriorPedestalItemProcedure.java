package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

public class SpawnWarriorPedestalItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		SpawnDungeonItemProcedure.execute(world, x + 0.5, y + 0.9, z + 0.5, "warrior");
	}
}