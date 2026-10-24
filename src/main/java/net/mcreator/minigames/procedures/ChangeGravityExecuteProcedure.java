package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChangeGravityExecuteProcedure {
	public static void execute(Direction gravity, Entity entity) {
		if (gravity == null || entity == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.gravity = gravity;
			_vars.markSyncDirty();
		}
	}
}