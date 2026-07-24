package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SettingsDisplayProcedure {
	public static String execute(LevelAccessor world) {
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		String line5 = "";
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode) {
			line1 = "Hunted gains a " + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).WhenPVPActive) + "s head start";
		} else {
			line1 = "PvP enables after " + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).WhenPVPActive) + "s";
		}
		if ((world instanceof ServerLevel _serverLevelGR0 && _serverLevelGR0.getGameRules().get(GameRules.KEEP_INVENTORY)) == false) {
			line2 = "Keep Inventory is active";
		} else {
			line2 = "Keep Inventory is active";
		}
		line3 = "Health is set to " + new java.text.DecimalFormat("##").format(MinigamesModVariables.health);
		return "Match Settings:" + "\n" + line1 + "\n" + line2 + "\n" + line3;
	}
}