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

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.network.MinigamesModVariables;

public class GlueHitsBlockProcedure {
	private static final int GLUE_RADIUS = 20;
	private static final int GLUE_DURATION_TICKS = 200;

	public static void execute(LevelAccessor world, double x, double y, double z) {
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX;
		int glueY = (int) Math.floor(y);
		int minX = (int) arenaCenter.x() - GLUE_RADIUS;
		int maxX = (int) arenaCenter.x() + GLUE_RADIUS;
		int minZ = (int) arenaCenter.z() - GLUE_RADIUS;
		int maxZ = (int) arenaCenter.z() + GLUE_RADIUS;

		playGlueSound(world, x, y, z, 2.0F);
		applyGlueLayer(world, glueY, minX, maxX, minZ, maxZ);

		MinigamesMod.queueServerWork(GLUE_DURATION_TICKS, () -> {
			playGlueSound(world, x, y, z, 0.7F);
			clearGlueLayer(world, glueY, minX, maxX, minZ, maxZ);
		});
	}

	private static void applyGlueLayer(LevelAccessor world, int y, int minX, int maxX, int minZ, int maxZ) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int targetX = minX; targetX <= maxX; targetX++) {
			for (int targetZ = minZ; targetZ <= maxZ; targetZ++) {
				mutablePos.set(targetX, y, targetZ);
				if (world.getBlockState(mutablePos).getBlock() == Blocks.AIR) {
					world.setBlock(mutablePos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
			}
		}
	}

	private static void clearGlueLayer(LevelAccessor world, int y, int minX, int maxX, int minZ, int maxZ) {
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
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.slime_block.break block @a ~ ~ ~ 2 " + pitch);
		}
	}
}
