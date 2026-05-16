package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class VoteYesPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!entity.getData(MinigamesModVariables.PLAYER_VARIABLES).voted && MinigamesModVariables.MapVariables.get(world).ActiveVote) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.voted = true;
				_vars.votedYes = true;
				_vars.markSyncDirty();
			}
		}
	}
}