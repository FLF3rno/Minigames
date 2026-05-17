package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class WonVoteProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).voteType == 0) {
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
	}
}