package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowAchievementHudProcedure {
	public static boolean execute(LevelAccessor world) {
		if (!(MinigamesModVariables.MapVariables.get(world).achievement == 0) && MinigamesModVariables.MapVariables.get(world).displayTimer == true) {
			return true;
		}
		return false;
	}
}