package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SettingsDisplayProcedure {
	public static String execute(LevelAccessor world) {
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		String line5 = "";
		if (MinigamesModVariables.MapVariables.get(world).nightVision == true) {
			line1 = "Permanent Night Vision is active";
		} else {
			line1 = "Permanent Night Vision is NOT active";
		}
		if ((world instanceof ServerLevel _serverLevelGR0 && _serverLevelGR0.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) == false) {
			line2 = "Keep Inventory is active";
		} else {
			line2 = "Keep Inventory is active";
		}
		if (MinigamesModVariables.MapVariables.get(world).minimap == true) {
			line4 = "Minimap is shown";
		} else {
			line4 = "Minimap is not shown";
		}
		if (MinigamesModVariables.MapVariables.get(world).randomizeSpawn == true) {
			line5 = "Items and location is kept on game start ";
		} else {
			line5 = "Items and location is reset on game start ";
		}
		line3 = "Health is set to " + new java.text.DecimalFormat("##").format(MinigamesModVariables.health);
		return ((("Match Settings:" + "\n") + "" + (line1 + "\n")) + "" + (line2 + "\n")) + "" + ((line3 + "\n") + "" + (line4 + "" + ("\n" + line5)));
	}
}