package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class SpawnThiefPedestalItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		SpawnDungeonItemProcedure.execute(world, x + 0.5, y + 0.9, z + 0.5, "thief");
		world.setBlock(BlockPos.containing(x + 0.5, y, z + 0.5), Blocks.OCHRE_FROGLIGHT.defaultBlockState(), 3);
	}
}