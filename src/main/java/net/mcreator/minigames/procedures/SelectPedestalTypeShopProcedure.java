package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class SelectPedestalTypeShopProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD)) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension"))) {
			if (Mth.nextInt(RandomSource.create(), 1, 3) == 1) {
				if (Mth.nextInt(RandomSource.create(), 1, 100) <= 85) {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.SINGLE_SHOP_PEDESTAL.get().defaultBlockState(), 3);
				} else {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.TEAM_SHOP_PEDESTAL.get().defaultBlockState(), 3);
				}
			} else {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}
}