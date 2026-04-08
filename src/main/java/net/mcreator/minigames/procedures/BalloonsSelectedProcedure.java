package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class BalloonsSelectedProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 1) == 1) {
			return true;
		}
		return false;
	}
}