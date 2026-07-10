package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayHours2Procedure {
	public static double execute(LevelAccessor world) {
		if (((MinigamesModVariables.MapVariables.get(world).gameHours + "").replace(".0", "")).length() == 2) {
			return parseDouble((new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameHours)).substring(1, 2));
		}
		return parseDouble((new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameHours)).substring(0, 1));
	}

	private static double parseDouble(String s) {
		try {
			return Double.parseDouble(s.trim());
		} catch (Exception e) {
			return 0;
		}
	}
}