package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

public class GetAchievementInfoProcedure {
	public static void execute(LevelAccessor world) {
		if (!(world instanceof ServerLevel serverLevel)) {
			return;
		}

		AdvancementHolder advancement = AchievementObtainedProcedure.getTargetAdvancement(serverLevel);

		if (advancement != null) {
			var display = advancement.value().display().orElse(null);

			if (display != null) {
				MinigamesModVariables.MapVariables.get(world).AchievementTitle = display.getTitle().getString();
				MinigamesModVariables.MapVariables.get(world).AchievementDescription = display.getDescription().getString();
				MinigamesModVariables.MapVariables.get(world).AchievementIcon = display.getIcon().create();
			}
		}

		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}