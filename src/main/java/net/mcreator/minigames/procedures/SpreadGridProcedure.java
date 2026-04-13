package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class SpreadGridProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double neighbour = 0;
		neighbour = 0;
		if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == MinigamesModBlocks.EMPTY_GRID_BLOCK.get()) {
			if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == MinigamesModBlocks.ROOM_GRID_BLOCK.get()) {
				neighbour = neighbour + 1;
			}
			if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == MinigamesModBlocks.ROOM_GRID_BLOCK.get()) {
				neighbour = neighbour + 1;
			}
			if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == MinigamesModBlocks.ROOM_GRID_BLOCK.get()) {
				neighbour = neighbour + 1;
			}
			if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == MinigamesModBlocks.ROOM_GRID_BLOCK.get()) {
				neighbour = neighbour + 1;
			}
			if (neighbour <= 1) {
				if (Math.random() < 0.5) {
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
				}
			}
		}
	}
}