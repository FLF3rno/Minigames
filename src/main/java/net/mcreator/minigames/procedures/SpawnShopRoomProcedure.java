package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;

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
		ServerLevel dungeonLevel = ((ServerLevel) world).getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension")));
		MinigamesMod.queueServerWork(25, () -> {
			int minX = (int) Math.floor(x);
			int minY = (int) Math.floor(y);
			int minZ = (int) Math.floor(z);
			for (int sx = minX; sx < minX + 31; sx++) {
				for (int sy = minY; sy < minY + 29; sy++) {
					for (int sz = minZ; sz < minZ + 34; sz++) {
						BlockPos pos = new BlockPos(sx, sy, sz);
						if (dungeonLevel.getBlockState(pos).is(MinigamesModBlocks.EMPTY_SHOP_PEDESTAL.get())) {
							SelectPedestalTypeShopProcedure.execute(dungeonLevel, sx, sy, sz);
						}
					}
				}
			}
		});
	}
}