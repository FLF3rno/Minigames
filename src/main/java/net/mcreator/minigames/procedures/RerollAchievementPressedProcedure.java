package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class RerollAchievementPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).ready == true) {
			MinigamesModVariables.MapVariables.get(world).playersReady = MinigamesModVariables.MapVariables.get(world).playersReady - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).wantsToReroll == false) {
			MinigamesModVariables.MapVariables.get(world).rerollingPlayers = MinigamesModVariables.MapVariables.get(world).rerollingPlayers + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1) {
				MinigamesModVariables.MapVariables.get(world).p1state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 2) {
				MinigamesModVariables.MapVariables.get(world).p2state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 3) {
				MinigamesModVariables.MapVariables.get(world).p3state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 4) {
				MinigamesModVariables.MapVariables.get(world).p4state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 5) {
				MinigamesModVariables.MapVariables.get(world).p5state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else {
				MinigamesModVariables.MapVariables.get(world).p6state = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.ready = false;
				_vars.wantsToReroll = true;
				_vars.markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).players == MinigamesModVariables.MapVariables.get(world).rerollingPlayers) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minigames:rolldice player @a ~ ~ ~ 1000000000000 1");
					}
				}
				if (!world.isClientSide()) {
					MinigamesModVariables.MapVariables.get(world).achievmentType = Mth.nextInt(RandomSource.create(), 1, 4);
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					MinigamesMod.queueServerWork(2, () -> {
						ChooseAchievementProcedure.execute(world);
					});
				}
			} else {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound block.beacon.activate player @a ~ ~ ~ 1000000000000 2");
					}
				}
			}
		}
	}
}