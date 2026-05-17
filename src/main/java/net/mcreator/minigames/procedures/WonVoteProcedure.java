package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

public class WonVoteProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).respawningPlayers == 0) {
			if (MinigamesModVariables.MapVariables.get(world).voteType == 0) {
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = false;
				MinigamesModVariables.MapVariables.get(world).headStart = false;
				MinigamesModVariables.MapVariables.get(world).animateHunter = false;
				MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = false;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				StartRollTypeProcedure.execute(world, x, y, z, entity);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 1) {
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				StartRollTypeProcedure.execute(world, x, y, z, entity);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 2) {
				ResetCrownHuntProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 3) {
				StartSpleefProcedure.execute(world, x, y, z);
			}
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A7cNot all players are alive!"), false);
		}
	}
}