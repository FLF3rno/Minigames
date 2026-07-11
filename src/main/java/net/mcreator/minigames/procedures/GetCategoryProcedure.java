package net.mcreator.minigames.procedures;

public class GetCategoryProcedure {
	public static String execute(double achievementCategory) {
		if (achievementCategory == 1) {
			return "Story";
		} else if (achievementCategory == 2) {
			return "Nether";
		} else if (achievementCategory == 3) {
			return "End";
		} else if (achievementCategory == 4) {
			return "Adventure";
		} else if (achievementCategory == 5) {
			return "Husbandry";
		}
		return "null";
	}
}