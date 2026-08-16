package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChooseRandomBossProcedure {
	public static void execute(LevelAccessor world) {
		double rng = 0;
		if ((MinigamesModVariables.MapVariables.get(world).floorTypeDungeon).equals("church")) {
			rng = Mth.nextInt(RandomSource.create(), 1, 1);
			if (rng == 1) {
				MinigamesModVariables.MapVariables.get(world).bossName = "Flavio";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
		MinigamesModVariables.MapVariables.get(world).bossNumber = rng;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}