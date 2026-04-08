package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModBlocks;

@EventBusSubscriber
public class ReapplyGlueLayerProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		LevelAccessor world = event.getLevel();
		BlockPos pos = event.getPos();
		if (!isInsideActiveGlueLayer(world, pos)) {
			return;
		}
		MinigamesMod.queueServerWork(1, () -> {
			if (isInsideActiveGlueLayer(world, pos) && world.getBlockState(pos).getBlock() == Blocks.AIR) {
				world.setBlock(pos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
			}
		});
	}

	private static boolean isInsideActiveGlueLayer(LevelAccessor world, BlockPos pos) {
		BlockPos minAnchor = BlockPos.containing(-15, pos.getY(), -15);
		BlockPos maxAnchor = BlockPos.containing(15, pos.getY(), 15);
		return pos.getX() >= -15 && pos.getX() <= 15 && pos.getZ() >= -15 && pos.getZ() <= 15
				&& (world.getBlockState(minAnchor).is(MinigamesModBlocks.SPREADING_GLUE.get()) || world.getBlockState(maxAnchor).is(MinigamesModBlocks.SPREADING_GLUE.get()));
	}
}
