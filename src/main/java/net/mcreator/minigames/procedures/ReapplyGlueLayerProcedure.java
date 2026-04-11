package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.network.MinigamesModVariables;
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
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX;
		int minX = (int) arenaCenter.x() - 15;
		int maxX = (int) arenaCenter.x() + 15;
		int minZ = (int) arenaCenter.z() - 15;
		int maxZ = (int) arenaCenter.z() + 15;
		BlockPos minAnchor = BlockPos.containing(minX, pos.getY(), minZ);
		BlockPos maxAnchor = BlockPos.containing(maxX, pos.getY(), maxZ);
		return pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ
				&& (world.getBlockState(minAnchor).is(MinigamesModBlocks.SPREADING_GLUE.get()) || world.getBlockState(maxAnchor).is(MinigamesModBlocks.SPREADING_GLUE.get()));
	}
}
