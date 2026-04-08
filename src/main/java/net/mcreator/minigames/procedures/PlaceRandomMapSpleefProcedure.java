package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class PlaceRandomMapSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double rng = 0;
		rng = Mth.nextInt(RandomSource.create(), 1, 1);
		if (rng == 1) {
			PlaceBalloonsSpleefProcedure.execute(world, x, y, z);
		}
	}
}