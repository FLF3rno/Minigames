package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class WinOverlayDisplayProcedure {
	public static boolean execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).winAnimationState != -1 && MinigamesModVariables.MapVariables.get(world).hunteraWinAnimation == false) {
			return true;
		}
		return false;
	}
}
