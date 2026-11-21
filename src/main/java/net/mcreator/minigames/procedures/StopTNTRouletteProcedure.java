package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

public class StopTNTRouletteProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).playingTNTRoulette = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.PVP, true, world.getServer());
	}
}