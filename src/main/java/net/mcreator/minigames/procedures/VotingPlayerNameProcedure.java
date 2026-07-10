package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class VotingPlayerNameProcedure {
	public static String execute(LevelAccessor world) {
		if (!(MinigamesModVariables.VotingEntity == null)) {
			return MinigamesModVariables.MapVariables.get(world).VotingPlayerName;
		}
		return "null";
	}
}