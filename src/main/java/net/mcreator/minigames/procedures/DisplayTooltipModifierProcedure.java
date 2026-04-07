package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayTooltipModifierProcedure {
	public static boolean execute(LevelAccessor world) {
		if (!(MinigamesModVariables.MapVariables.get(world).achievement == 43) && !(MinigamesModVariables.MapVariables.get(world).achievement == 39)) {
			return false;
		}
		return true;
	}
}