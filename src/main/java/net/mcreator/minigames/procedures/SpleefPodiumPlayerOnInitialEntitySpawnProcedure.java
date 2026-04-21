package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

public class SpleefPodiumPlayerOnInitialEntitySpawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.GOLD_BLOCK) {
			if (entity instanceof SpleefPodiumPlayerEntity _datEntSetI)
				_datEntSetI.getEntityData().set(SpleefPodiumPlayerEntity.DATA_position, 1);
		} else if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.IRON_BLOCK) {
			if (entity instanceof SpleefPodiumPlayerEntity _datEntSetI)
				_datEntSetI.getEntityData().set(SpleefPodiumPlayerEntity.DATA_position, 2);
		} else if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.WAXED_COPPER_BLOCK) {
			if (entity instanceof SpleefPodiumPlayerEntity _datEntSetI)
				_datEntSetI.getEntityData().set(SpleefPodiumPlayerEntity.DATA_position, 3);
		}
	}
}