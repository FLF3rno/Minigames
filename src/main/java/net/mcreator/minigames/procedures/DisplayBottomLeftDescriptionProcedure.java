package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplayBottomLeftDescriptionProcedure {
	public static String execute(LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode == true) {
			return "Hunted gains a " + (new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).WhenPVPActive) + "s head start");
		}
		return "PvP enables after " + (new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).WhenPVPActive) + "s");
	}
}