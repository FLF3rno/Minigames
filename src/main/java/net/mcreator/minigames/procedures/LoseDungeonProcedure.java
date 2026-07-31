package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

public class LoseDungeonProcedure {
	public static void execute(LevelAccessor world) {
		EndDungeonProcedure.execute(world);
	}
}