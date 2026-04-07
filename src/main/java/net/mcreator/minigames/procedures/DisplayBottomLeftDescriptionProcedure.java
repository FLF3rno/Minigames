package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayBottomLeftDescriptionProcedure {
	public static String execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).headStart == true) {
			return "Hunted gains a " + (new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).debuffLength) + "s head start");
		}
		return "PVP enables after 5 minutes";
	}
}