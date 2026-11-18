package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class BoulderProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.AIR
				&& (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.AIR) {
			BlockBreakSimulationProcedure.execute(world, x, y, z, world.getBlockState(BlockPos.containing(x, y, z)), true, true);
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			if ((world.getBlockState(BlockPos.containing(x + 1, y + 1, z))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x - 1, y + 1, z))).getBlock() == Blocks.AIR
					&& (world.getBlockState(BlockPos.containing(x, y + 1, z + 1))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x, y + 1, z - 1))).getBlock() == Blocks.AIR) {
				BlockBreakSimulationProcedure.execute(world, x, y, z, world.getBlockState(BlockPos.containing(x, y + 1, z)), true, true);
				world.setBlock(BlockPos.containing(x, y + 1, z), Blocks.AIR.defaultBlockState(), 3);
			}
			if ((world.getBlockState(BlockPos.containing(x + 1, y - 1, z))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x - 1, y - 1, z))).getBlock() == Blocks.AIR
					&& (world.getBlockState(BlockPos.containing(x, y - 1, z + 1))).getBlock() == Blocks.AIR && (world.getBlockState(BlockPos.containing(x, y - 1, z - 1))).getBlock() == Blocks.AIR) {
				BlockBreakSimulationProcedure.execute(world, x, y, z, world.getBlockState(BlockPos.containing(x, y - 1, z)), true, true);
				world.setBlock(BlockPos.containing(x, y - 1, z), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}
}