package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ActivateSolarSystemProcedure {
	public static void execute(LevelAccessor world) {
		double num = 0;
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 2) == 2) {
			MinigamesModVariables.MapVariables.get(world).mapsSpleef = MinigamesModVariables.MapVariables.get(world).mapsSpleef - 2;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).mapsSpleef = MinigamesModVariables.MapVariables.get(world).mapsSpleef + 2;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}