package net.mcreator.minigames.procedures;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ReadyUpProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _worldorig = world;
			world = _origLevel.getServer().getLevel(Level.OVERWORLD);
			if (world != null) {
				{
					Entity _entityTeam = entity;
					PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam((entity.getDisplayName().getString()));
					if (_pt != null) {
						if (_entityTeam instanceof Player _player)
							_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().getName(), _pt);
						else
							_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
					}
				}
			}
			world = _worldorig;
		}
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).wantsToReroll == true) {
			MinigamesModVariables.MapVariables.get(world).rerollingPlayers = MinigamesModVariables.MapVariables.get(world).rerollingPlayers - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		ClosingHunterAnimationProcedure.execute(world, entity);
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/setworldspawn ~ ~ ~");
			}
		}
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.wantsToReroll = false;
			_vars.ready = true;
			_vars.markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).playersReady = MinigamesModVariables.MapVariables.get(world).playersReady + 1;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (MinigamesModVariables.MapVariables.get(world).players == MinigamesModVariables.MapVariables.get(world).playersReady) {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound block.beacon.deactivate player @a ~ ~ ~ 1000000000000 0.4");
				}
			}
			StartGameAchieverunProcedure.execute(world, x, y, z);
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound block.beacon.activate player @a ~ ~ ~ 1000000000000 2");
				}
			}
		}
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1) {
			MinigamesModVariables.MapVariables.get(world).p1state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 2) {
			MinigamesModVariables.MapVariables.get(world).p2state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 3) {
			MinigamesModVariables.MapVariables.get(world).p3state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 4) {
			MinigamesModVariables.MapVariables.get(world).p4state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 5) {
			MinigamesModVariables.MapVariables.get(world).p5state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).p6state = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}