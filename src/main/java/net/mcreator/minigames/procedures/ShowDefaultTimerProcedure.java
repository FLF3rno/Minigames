package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowDefaultTimerProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (MinigamesModVariables.MapVariables.get(world).timertype == 0 && MinigamesModVariables.MapVariables.get(world).displayTimer == true) {
			return true;
		}
		return false;
	}
}