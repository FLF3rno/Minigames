package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class AchievementInitiateGameProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		TeleportAchievementProcedure.execute(world);
		GameCountdownProcedure.execute(world, x, y, z);
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.AchievementLobbyState = "";
				_vars.markSyncDirty();
			}
			if (entityiterator instanceof Player _player)
				_player.closeContainer();
			if (entityiterator instanceof ServerPlayer _player)
				_player.setGameMode(GameType.SURVIVAL);
		}
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			MinigamesMod.queueServerWork(80, () -> {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerSpeed = 1;
					_vars.markSyncDirty();
				}
			});
			MinigamesMod.queueServerWork((int) (MinigamesModVariables.MapVariables.get(world).WhenPVPActive + 80), () -> {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.TimerColor = "C7C7C7";
					_vars.markSyncDirty();
				}
			});
		}
	}
}