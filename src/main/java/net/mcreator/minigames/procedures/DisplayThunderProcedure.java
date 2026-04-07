package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayThunderProcedure {
	public static double execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).achievement == 39) {
			return 3;
		}
		if (MinigamesModVariables.MapVariables.get(world).achievement == 43) {
			return 4;
		}
		return 0;
	}
}