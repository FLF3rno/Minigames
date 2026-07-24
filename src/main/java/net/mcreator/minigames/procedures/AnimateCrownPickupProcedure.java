package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class AnimateCrownPickupProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).ShowTimer = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				Entity _ent = entityiterator;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
							LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minigames:teameliminated ui @s ~ ~ ~ 1 1");
				}
			}
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.timerSpeed = -1;
				_vars.timerTick = 0;
				_vars.timerSeconds = 1;
				_vars.timerMinutes = MinigamesModVariables.MapVariables.get(world).crownMinutes;
				_vars.timerHours = 0;
				_vars.markSyncDirty();
			}
		}
	}
}