package net.mcreator.minigames.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBreakSimulationProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState block, boolean particles, boolean sound) {
		if (!(world instanceof Level level) || level.isClientSide()) {
			return;
		}

		if (block == null || block.isAir()) {
			return;
		}

		BlockPos pos = BlockPos.containing(x, y, z);

		if (particles) {
			level.levelEvent(2001, pos, Block.getId(block));
		}

		if (sound) {
			level.playSound(
					null,
					x,
					y,
					z,
					block.getSoundType().getBreakSound(),
					SoundSource.BLOCKS,
					(block.getSoundType().getVolume() + 1.0F) / 2.0F,
					block.getSoundType().getPitch() * 0.8F
			);
		}
	}
}