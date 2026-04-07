package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class AnimatePVPProcedure {
	public static double execute(LevelAccessor world) {
		double anim = 0;
		return MinigamesModVariables.MapVariables.get(world).pvpstate;
	}
}