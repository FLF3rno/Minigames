package net.mcreator.minigames.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.network.MinigamesModVariables;

public class GlueHitsBlockProcedure {
	private static final int GLUE_RADIUS = 20;
	private static final int GLUE_DURATION_TICKS = 200;
	private static final int BASE_Y = 100;

	public static void execute(LevelAccessor world, double x, double y, double z) {
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX;
		int glueY = getNearestLayerY(world, y);
		int minX = (int) arenaCenter.x() - GLUE_RADIUS;
		int maxX = (int) arenaCenter.x() + GLUE_RADIUS;
		int minZ = (int) arenaCenter.z() - GLUE_RADIUS;
		int maxZ = (int) arenaCenter.z() + GLUE_RADIUS;

		playGlueSound(world, x, y, z, 2.0F);
		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(world);
		ReapplyGlueLayerProcedure.setGlueLayerTicks(world, glueY, GLUE_DURATION_TICKS);
		mapVariables.markSyncDirty();
		applyGlueLayer(world, glueY, minX, maxX, minZ, maxZ);
	}

	private static void applyGlueLayer(LevelAccessor world, int y, int minX, int maxX, int minZ, int maxZ) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int targetX = minX; targetX <= maxX; targetX++) {
			for (int targetZ = minZ; targetZ <= maxZ; targetZ++) {
				mutablePos.set(targetX, y, targetZ);
				if (canGlueOccupy(world, mutablePos)) {
					world.setBlock(mutablePos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
			}
		}
	}

	public static void clearGlueLayer(LevelAccessor world, int y, int minX, int maxX, int minZ, int maxZ) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int targetX = minX; targetX <= maxX; targetX++) {
			for (int targetZ = minZ; targetZ <= maxZ; targetZ++) {
				mutablePos.set(targetX, y, targetZ);
				if (world.getBlockState(mutablePos).is(MinigamesModBlocks.SPREADING_GLUE.get())) {
					world.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	private static void playGlueSound(LevelAccessor world, double x, double y, double z, float pitch) {
		if (world instanceof ServerLevel level) {
			level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.slime_block.break block @a ~ ~ ~ 2 " + pitch);
		}
	}

	private static int getNearestLayerY(LevelAccessor world, double impactY) {
		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(world);
		int layersRemaining = Math.max(1, (int) mapVariables.layersRemainingSpleef);
		int gap = (int) mapVariables.gapBetweenLayersSpleef;
		int closestY = BASE_Y;
		double closestDistance = Double.MAX_VALUE;
		for (int layer = 1; layer <= layersRemaining; layer++) {
			int candidateY = BASE_Y + (layer - 1) * gap;
			double distance = Math.abs(impactY - candidateY);
			if (distance < closestDistance) {
				closestDistance = distance;
				closestY = candidateY;
			}
		}
		return closestY;
	}

	public static boolean canGlueOccupy(LevelAccessor world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.isAir() || state.canBeReplaced();
	}
}




