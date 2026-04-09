package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SolarSystemSelectedProcedure {
	public static boolean execute(LevelAccessor world) {
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 2) == 2) {
			return true;
		}
		return false;
	}
}