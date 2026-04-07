package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowCompassHunterProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (!(entity.getDisplayName().getString()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievement) && MinigamesModVariables.MapVariables.get(world).achievementHunterMode == true
				&& MinigamesModVariables.MapVariables.get(world).achievement != -1) {
			return true;
		}
		return false;
	}
}