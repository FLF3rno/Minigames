package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

public class BreakDoorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y, z))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			BreakDoorProcedure.execute(world, x + 1, y, z);
			BreakDoorProcedure.execute(world, x - 1, y, z);
			BreakDoorProcedure.execute(world, x, y + 1, z);
			BreakDoorProcedure.execute(world, x, y - 1, z);
			BreakDoorProcedure.execute(world, x, y, z + 1);
			BreakDoorProcedure.execute(world, x, y, z - 1);
		}
	}
}