package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlayingMinigameProcedure {
	public static boolean execute(LevelAccessor world) {
		return MinigamesModVariables.MapVariables.get(world).playingAchievement || MinigamesModVariables.MapVariables.get(world).CrownHuntInGame || MinigamesModVariables.MapVariables.get(world).playingSpleef;
	}
}