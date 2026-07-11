package net.mcreator.minigames.procedures;

public class GetCategoryTextureProcedure {
	public static String execute(double achievementCategory) {
		if (achievementCategory == 1) {
			return "minecraft:textures/block/stone.png";
		} else if (achievementCategory == 2) {
			return "minecraft:textures/block/netherrack.png";
		} else if (achievementCategory == 3) {
			return "minecraft:textures/block/end_stone.png";
		} else if (achievementCategory == 4) {
			return "minecraft:textures/block/sandstone_top.png";
		} else if (achievementCategory == 5) {
			return "minecraft:textures/block/farmland.png";
		}
		return "minecraft:textures/block/stone.png";
	}
}