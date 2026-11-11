package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class SpawnShopRoomProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String prefix = "";
		double rng = 0;
		prefix = "dungeon_shop_";
		rng = Mth.nextInt(RandomSource.create(), 1, 1);
		if (rng == 1) {
			prefix = "dungeon_shop_normal";
		}
		SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), x, y, z, "none", prefix);
	}
}