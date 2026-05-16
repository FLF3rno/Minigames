package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.MinigamesModVariables;

public class VotingPlayerNameProcedure {
	public static String execute() {
		if (!(MinigamesModVariables.VotingEntity == null)) {
			return MinigamesModVariables.VotingEntity.getDisplayName().getString();
		}
		return "null";
	}
}