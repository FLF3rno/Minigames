package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

public class WinSpleefProcedure {
	public static void execute(LevelAccessor world) {
		StopSpleefProcedure.execute(world);
	}
}