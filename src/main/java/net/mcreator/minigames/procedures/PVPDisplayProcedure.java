package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PVPDisplayProcedure {
	public static double execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).achievement != -1 && MinigamesModVariables.MapVariables.get(world).gameMinutes >= 5) {
			return 1;
		}
		return 0;
	}
}