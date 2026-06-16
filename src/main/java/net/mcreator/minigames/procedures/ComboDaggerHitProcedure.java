package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ComboDaggerHitProcedure {
	public static void execute(Entity sourceentity) {
		if (sourceentity == null)
			return;
		if (sourceentity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown > 0) {
			{
				MinigamesModVariables.PlayerVariables _vars = sourceentity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.dashCooldown = 0;
				_vars.markSyncDirty();
			}
		}
	}
}