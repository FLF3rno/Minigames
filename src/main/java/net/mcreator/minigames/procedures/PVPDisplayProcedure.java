package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PVPDisplayProcedure {
	public static double execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return 0;
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement && entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes >= 5) {
			return 1;
		}
		return 0;
	}
}