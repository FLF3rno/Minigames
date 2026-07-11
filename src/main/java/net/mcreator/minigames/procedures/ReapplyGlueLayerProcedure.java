package net.mcreator.minigames.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ReapplyGlueLayerProcedure {
	public static void setGlueLayerTicks(LevelAccessor world, int glueY, int ticks) {
		BlockEntity blockEntity = world.getBlockEntity(BlockPos.containing(0, glueY, 0));
		if (blockEntity != null) {
			blockEntity.getPersistentData().putDouble("glueLayerTicks", ticks);
		}
	}

	public static <T> T execute(Object... args) {
		return null;
	}
}

