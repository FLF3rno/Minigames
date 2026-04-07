package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayPVPProcedure {
	public static boolean execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).pvpstate == -1) {
			return false;
		}
		return true;
	}
}