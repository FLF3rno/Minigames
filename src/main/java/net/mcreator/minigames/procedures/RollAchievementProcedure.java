package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

public class RollAchievementProcedure {
	public static void execute(LevelAccessor world) {
		double achievement = 0;
		MinigamesModVariables.MapVariables.get(world).AchievementCategory = Mth.nextInt(RandomSource.create(), 1, 5);
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (MinigamesModVariables.MapVariables.get(world).AchievementCategory == 1) {
			achievement = Mth.nextInt(RandomSource.create(), 1, 15);
		} else if (MinigamesModVariables.MapVariables.get(world).AchievementCategory == 2) {
			achievement = Mth.nextInt(RandomSource.create(), 1, 22);
		} else if (MinigamesModVariables.MapVariables.get(world).AchievementCategory == 3) {
			if (Math.random() < 0.85) {
				RollAchievementProcedure.execute(world);
			} else {
				achievement = Mth.nextInt(RandomSource.create(), 1, 1);
			}
		} else if (MinigamesModVariables.MapVariables.get(world).AchievementCategory == 4) {
			achievement = Mth.nextInt(RandomSource.create(), 1, 43);
		} else if (MinigamesModVariables.MapVariables.get(world).AchievementCategory == 5) {
			achievement = Mth.nextInt(RandomSource.create(), 1, 27);
		}
		MinigamesModVariables.MapVariables.get(world).Achievement = achievement;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		GetAchievementInfoProcedure.execute(world);
		ModifiersAchievementProcedure.execute(world);
	}
}