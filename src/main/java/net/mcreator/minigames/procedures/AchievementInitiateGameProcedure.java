package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class AchievementInitiateGameProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		TeleportAchievementProcedure.execute(world);
		GameCountdownProcedure.execute(world, x, y, z);
		MinigamesModVariables.MapVariables.get(world).ShowTimer = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel serverLevel) {
			for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
				MinigamesModVariables.PlayerVariables _vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.AchievementLobbyState = "";
				_vars.markSyncDirty();
				player.closeContainer();
				player.removeEffect(MinigamesModMobEffects.BLESSED);
				player.removeEffect(MobEffects.RESISTANCE);
			}
			MinigamesMod.queueServerWork(80, () -> {
				for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
					MinigamesModVariables.PlayerVariables _vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerSpeed = 1;
					_vars.markSyncDirty();
				}
			});
			int pvpDelay = (int) (MinigamesModVariables.MapVariables.get(world).WhenPVPActive + 80);
			MinigamesMod.queueServerWork(pvpDelay, () -> {
				for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
					MinigamesModVariables.PlayerVariables _vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.TimerColor = "C7C7C7";
					_vars.markSyncDirty();
				}
			});
		} else {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.AchievementLobbyState = "";
				_vars.markSyncDirty();
				if (entityiterator instanceof Player _player)
					_player.closeContainer();
				if (entityiterator instanceof LivingEntity _entity)
					_entity.removeEffect(MinigamesModMobEffects.BLESSED);
				if (entityiterator instanceof LivingEntity _entity)
					_entity.removeEffect(MobEffects.RESISTANCE);
			}
			MinigamesMod.queueServerWork(80, () -> {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerSpeed = 1;
					_vars.markSyncDirty();
				}
			});
		}
	}
}