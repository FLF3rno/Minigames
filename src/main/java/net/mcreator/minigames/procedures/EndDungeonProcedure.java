package net.mcreator.minigames.procedures;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class EndDungeonProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).playingDungeons = false;
		MinigamesModVariables.MapVariables.get(world).minimap = false;
		MinigamesModVariables.MapVariables.get(world).waypoints = false;
		MinigamesModVariables.MapVariables.get(world).extraItemRooms = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		ResetStatsProcedure.execute(world);
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof Player) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.showOnlyHearts = false;
					_vars.canDash = false;
					_vars.markSyncDirty();
				}
				GrantGameCompassProcedure.execute(world, entityiterator);
			}
		}
		if (world instanceof Level _level) {
			PlayerTeam _pt = _level.getScoreboard().getPlayerTeam("dungeon_players");
			if (_pt != null)
				_level.getScoreboard().removePlayerTeam(_pt);
		}
		if (world instanceof Level _level) {
			PlayerTeam _pt = _level.getScoreboard().getPlayerTeam("dungeon_mobs");
			if (_pt != null)
				_level.getScoreboard().removePlayerTeam(_pt);
		}
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.FALL_DAMAGE, true, world.getServer());
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, true, world.getServer());
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, true, world.getServer());
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 128, world.getServer());
		MinigamesModVariables.MapVariables.get(world).currentRoomType = "none";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}