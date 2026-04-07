package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class TooltipModifierProcedure {
	public static String execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).achievement == 39) {
			return "Weather will permanently be Thunder";
		}
		if (MinigamesModVariables.MapVariables.get(world).achievement == 43) {
			return "Time will start as Night";
		}
		return "";
	}
}