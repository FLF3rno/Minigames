package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayEntityReady6Procedure {
	public static double execute(LevelAccessor world) {
		return MinigamesModVariables.MapVariables.get(world).p6state;
	}
}