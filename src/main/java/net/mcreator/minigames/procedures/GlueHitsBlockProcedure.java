package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.network.MinigamesModVariables;

public class GlueHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double centerX = MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x();
		double centerZ = MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z();
		int minX = (int) centerX - 15;
		int maxX = (int) centerX + 15;
		int minZ = (int) centerZ - 15;
		int maxZ = (int) centerZ + 15;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.slime_block.break block @a ~ ~ ~ 2 2");
		for (int targetX = minX; targetX <= maxX; targetX++) {
			for (int targetZ = minZ; targetZ <= maxZ; targetZ++) {
				BlockPos pos = BlockPos.containing(targetX, y, targetZ);
				if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
					world.setBlock(pos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
			}
		}
		world.setBlock(BlockPos.containing(minX, y, minZ), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
		world.setBlock(BlockPos.containing(maxX, y, maxZ), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
		MinigamesMod.queueServerWork(200, () -> {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/playsound minecraft:block.slime_block.break block @a ~ ~ ~ 2 0.7 1");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/fill " + maxX + " ~ " + maxZ + " " + minX + " ~ " + minZ + " air replace minigames:spreading_glue");
		});
	}
}
