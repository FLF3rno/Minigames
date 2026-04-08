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

import net.mcreator.minigames.init.MinigamesModBlocks;
public class SpreadingGlueBlockAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.slime_block.break block @a ~ ~ ~ 2 2");
		if (x <= 15 && x >= -15) {
			if (z <= 15 && z >= -15) {
				if ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == Blocks.AIR) {
					world.setBlock(BlockPos.containing(x + 1, y, z), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
				if ((world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == Blocks.AIR) {
					world.setBlock(BlockPos.containing(x - 1, y, z), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
				if ((world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == Blocks.AIR) {
					world.setBlock(BlockPos.containing(x, y, z + 1), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
				if ((world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == Blocks.AIR) {
					world.setBlock(BlockPos.containing(x, y, z - 1), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
				}
			}
		}
	}
}
