package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class HideNoMapsSelectedProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (MinigamesModVariables.MapVariables.get(world).mapsSpleef == 0) {
			return false;
		}
		return true;
	}
}