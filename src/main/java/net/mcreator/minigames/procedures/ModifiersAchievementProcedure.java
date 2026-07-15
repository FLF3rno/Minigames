package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ModifiersAchievementProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).AchievementModifier = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if ((GetAchievementProcedure.execute(MinigamesModVariables.MapVariables.get(world).AchievementCategory, MinigamesModVariables.MapVariables.get(world).Achievement)).equals("adventure/sleep_in_bed")) {
			MinigamesModVariables.MapVariables.get(world).AchievementModifier = 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if ((GetAchievementProcedure.execute(MinigamesModVariables.MapVariables.get(world).AchievementCategory, MinigamesModVariables.MapVariables.get(world).Achievement)).equals("adventure/lightning_rod_with_villager_no_fire")) {
			MinigamesModVariables.MapVariables.get(world).AchievementModifier = 2;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}