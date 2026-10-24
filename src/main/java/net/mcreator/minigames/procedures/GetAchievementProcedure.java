package net.mcreator.minigames.procedures;

import net.mcreator.minigames.util.DynamicAchievementList;
import net.minecraft.resources.Identifier;

public class GetAchievementProcedure {
	public static String execute(double AchievementCategory, double AchievementNumber) {
		Identifier currentTarget = DynamicAchievementList.getCurrentTargetId();
		if (currentTarget != null) {
			return currentTarget.toString();
		}
		String idStr = DynamicAchievementList.getIdString((int) AchievementNumber);
		if (!idStr.isEmpty()) {
			return idStr;
		}
		return "minecraft:story/mine_stone";
	}
}
