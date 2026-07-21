package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class TimerSecondsProcedure {
	public static double execute(Entity entity) {
		if (entity == null)
			return 0;
		double totSecs = 0;
		totSecs = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds;
		totSecs = totSecs + entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes * 60;
		totSecs = totSecs + entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerHours * 3600;
		return totSecs;
	}
}