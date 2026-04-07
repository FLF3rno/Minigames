package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayReminderProcedure {
	public static boolean execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).achievement == -1) {
			return false;
		}
		return true;
	}
}