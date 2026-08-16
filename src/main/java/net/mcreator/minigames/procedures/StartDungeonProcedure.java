package net.mcreator.minigames.procedures;

import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StartDungeonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MinigamesModVariables.MapVariables.get(world).playingDungeons = true;
		MinigamesModVariables.MapVariables.get(world).minimap = false;
		MinigamesModVariables.MapVariables.get(world).waypoints = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof Level _level)
			_level.getScoreboard().addPlayerTeam("dungeon_players");
		if (world instanceof Level _level)
			_level.getScoreboard().addPlayerTeam("dungeon_mobs");
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.showOnlyHearts = true;
				_vars.playerInInventory = true;
				_vars.canDash = true;
				_vars.playerSlots = 3;
				_vars.backpackSlots = 3;
				_vars.maxDashCooldown = 60;
				_vars.dashLength = 1;
				_vars.PassiveHealCooldown = 80;
				_vars.PassiveHealAmount = 1;
				_vars.markSyncDirty();
			}
			{
				Entity _entityTeam = entityiterator;
				PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam("dungeon_players");
				if (_pt != null) {
					if (_entityTeam instanceof Player _player)
						_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().name(), _pt);
					else
						_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
				}
			}
			if (entityiterator instanceof ServerPlayer _player)
				_player.setGameMode(GameType.ADVENTURE);
		}
		if (world instanceof Level _level) {
			PlayerTeam _pt = _level.getScoreboard().getPlayerTeam("dungeon_mobs");
			if (_pt != null) {
				_pt.setNameTagVisibility(Team.Visibility.NEVER);
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"effect give @a minecraft:saturation infinite 100 true");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"execute in minigames:dungeon_dimension run forceload add 10 10 -10 -10");
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.FALL_DAMAGE, false, world.getServer());
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, false, world.getServer());
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/xp set @a 100 levels");
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _switchworld11 = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension")));
			if (_switchworld11 != null) {
				worldSwitch11(_switchworld11, x, y, z);
			}
		}
	}

	private static void worldSwitch11(LevelAccessor world, double x, double y, double z) {
		ChooseFloorProcedure.execute(world, x, y, z, "church");
	}
}