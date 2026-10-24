package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.advancements.AdvancementHolder;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.DynamicAchievementList;

public class RollAchievementProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel serverLevel) {
			AdvancementHolder chosen = DynamicAchievementList.rollAchievement(serverLevel, 0.85);
			if (chosen != null) {
				int index = DynamicAchievementList.getAdvancements(serverLevel).indexOf(chosen);
				MinigamesModVariables.MapVariables.get(world).Achievement = index;
				DynamicAchievementList.setCurrentTarget(chosen.id());

				String path = chosen.id().getPath();
				if (path.startsWith("story/")) {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 1;
				} else if (path.startsWith("nether/")) {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 2;
				} else if (path.startsWith("end/")) {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 3;
				} else if (path.startsWith("adventure/")) {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 4;
				} else if (path.startsWith("husbandry/")) {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 5;
				} else {
					MinigamesModVariables.MapVariables.get(world).AchievementCategory = 4;
				}
			}
		}
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		GetAchievementInfoProcedure.execute(world);
		ModifiersAchievementProcedure.execute(world);
	}
}
