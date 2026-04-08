package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ActivateBalloonsProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double num = 0;
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 1) == 1) {
			MinigamesModVariables.MapVariables.get(world).mapsSpleef = MinigamesModVariables.MapVariables.get(world).mapsSpleef - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).mapsSpleef = MinigamesModVariables.MapVariables.get(world).mapsSpleef + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}