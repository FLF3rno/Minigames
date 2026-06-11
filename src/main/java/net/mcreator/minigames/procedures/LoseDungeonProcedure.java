package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

public class LoseDungeonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		EndDungeonProcedure.execute(world, x, y, z);
	}
}