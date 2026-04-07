package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DarkBackgroundProcedure {
	public static double execute(LevelAccessor world) {
		double state = MinigamesModVariables.MapVariables.get(world).winAnimationState;
		if (state < 5) {
			return state;
		} else if (state > 4) {
			return 5;
		}
		return 0;
	}
}
