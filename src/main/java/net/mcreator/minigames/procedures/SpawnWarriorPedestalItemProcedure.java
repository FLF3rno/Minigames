package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class SpawnWarriorPedestalItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		SpawnDungeonItemProcedure.execute(world, x + 0.5, y + 0.9, z + 0.5, "warrior");
		world.setBlock(BlockPos.containing(x + 0.5, y, z + 0.5), MinigamesModBlocks.RUBY_FROGLIGHT.get().defaultBlockState(), 3);
	}
}