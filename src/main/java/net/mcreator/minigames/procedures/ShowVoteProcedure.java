package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowVoteProcedure {
	public static boolean execute(LevelAccessor world) {
		return MinigamesModVariables.MapVariables.get(world).ActiveVote;
	}
}