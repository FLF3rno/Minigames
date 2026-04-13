package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

public class RoomPlacedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		SpreadGridProcedure.execute(world, x + 1, y, z);
		SpreadGridProcedure.execute(world, x - 1, y, z);
		SpreadGridProcedure.execute(world, x, y, z + 1);
		SpreadGridProcedure.execute(world, x, y, z - 1);
	}
}