package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.entity.VolleybombEntityEntity;
import net.mcreator.minigames.MinigamesMod;

public class VolleybombCheckCollisionProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof VolleybombEntityEntity _datEntL0 && _datEntL0.getEntityData().get(VolleybombEntityEntity.DATA_exploding))) {
			if (!((world.getBlockState(BlockPos.containing(x + 0.1, y, z))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x - 0.1, y, z))).getBlock() == Blocks.AIR)
					|| !((world.getBlockState(BlockPos.containing(x, y + 0.1, z))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x, y - 0.1, z))).getBlock() == Blocks.AIR)
					|| !((world.getBlockState(BlockPos.containing(x, y, z + 0.1))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x, y, z - 0.1))).getBlock() == Blocks.AIR)) {
				if (entity instanceof VolleybombEntityEntity _datEntSetL)
					_datEntSetL.getEntityData().set(VolleybombEntityEntity.DATA_exploding, true);
				MinigamesMod.queueServerWork(3, () -> {
					if (!((world.getBlockState(BlockPos.containing(x + 0.1, y, z))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x - 0.1, y, z))).getBlock() == Blocks.AIR)
							|| !((world.getBlockState(BlockPos.containing(x, y + 0.1, z))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x, y - 0.1, z))).getBlock() == Blocks.AIR)
							|| !((world.getBlockState(BlockPos.containing(x, y, z + 0.1))).getBlock() == Blocks.AIR) || !((world.getBlockState(BlockPos.containing(x, y, z - 0.1))).getBlock() == Blocks.AIR)) {
						VolleyBombExplode2Procedure.execute(world, x, y, z, entity);
					} else {
						if (entity instanceof VolleybombEntityEntity _datEntSetL)
							_datEntSetL.getEntityData().set(VolleybombEntityEntity.DATA_exploding, false);
					}
				});
			}
		}
	}
}