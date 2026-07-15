package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class AchievementInitiateGameProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		GameCountdownProcedure.execute(world, x, y, z);
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.AchievementLobbyState = "";
				_vars.markSyncDirty();
			}
			if (entityiterator instanceof Player _player)
				_player.closeContainer();
		}
	}
}