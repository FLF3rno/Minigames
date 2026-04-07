package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayBlankActuallyBlankProcedure {
	public static boolean execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).winAnimationState < 7) {
			return true;
		}
		return false;
	}
}
