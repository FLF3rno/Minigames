package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplaySpleefTimerProcedure {
	public static boolean execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef < 10) {
				return false;
			}
			if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef != 0) {
				return true;
			}
		}
		return false;
	}
}