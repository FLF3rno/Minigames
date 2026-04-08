package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlaceRandomMapSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double rng = 0;
		double NumberOfMaps = 0;
		NumberOfMaps = 1;
		rng = Mth.nextInt(RandomSource.create(), 1, (int) NumberOfMaps);
		if (rng == 1) {
			if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 1) == 1) {
				PlaceBalloonsSpleefProcedure.execute(world, x, y, z);
			} else {
				rng = Mth.nextInt(RandomSource.create(), 1, (int) NumberOfMaps);
			}
		}
	}
}