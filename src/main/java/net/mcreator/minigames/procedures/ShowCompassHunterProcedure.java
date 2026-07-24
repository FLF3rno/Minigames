package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowCompassHunterProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		return !(entity.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID) && MinigamesModVariables.MapVariables.get(world).achievementHunterMode
				&& MinigamesModVariables.MapVariables.get(world).playingAchievement;
	}
}