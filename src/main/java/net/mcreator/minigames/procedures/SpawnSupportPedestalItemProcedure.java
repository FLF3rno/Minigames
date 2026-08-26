package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class SpawnSupportPedestalItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		SpawnDungeonItemProcedure.execute(world, x + 0.5, y + 0.9, z + 0.5, "support");
		world.setBlock(BlockPos.containing(x + 0.5, y, z + 0.5), MinigamesModBlocks.AZURE_FROGLIGHT.get().defaultBlockState(), 3);
	}
}