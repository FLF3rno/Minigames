package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class RoomPlacedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double neighbour = 0;
		SpreadGridProcedure.execute(world, x + 1, y, z);
		SpreadGridProcedure.execute(world, x - 1, y, z);
		SpreadGridProcedure.execute(world, x, y, z + 1);
		SpreadGridProcedure.execute(world, x, y, z - 1);
		neighbour = 0;
		if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			neighbour = neighbour + 1;
		}
		if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			neighbour = neighbour + 1;
		}
		if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			neighbour = neighbour + 1;
		}
		if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			neighbour = neighbour + 1;
		}
		if (neighbour <= 1) {
			world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.END_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
		}
	}
}