package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlayingMinigameProcedure {
	public static boolean execute(LevelAccessor world) {
		boolean playin = false;
		playin = false;
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			playin = true;
		} else if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame) {
			playin = true;
		} else if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			playin = true;
		} else if (MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			playin = true;
		}
		return true;
	}
}