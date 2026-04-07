package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class ExpandCastleProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).is(BlockTags.create(ResourceLocation.parse("minigames:replace")))) {
			world.setBlock(BlockPos.containing(x, y - 1, z), MinigamesModBlocks.CASTLE_BRICKS.get().defaultBlockState(), 3);
		}
	}
}