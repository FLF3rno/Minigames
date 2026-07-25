package net.mcreator.minigames.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBreakSimulationProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState block, boolean particles, boolean sound) {
		if (!(world instanceof Level level)) {
			return;
		}

		if (block == null || block.isAir()) {
			return;
		}

		BlockPos pos = BlockPos.containing(x, y, z);

		if (particles) {
			if (!level.isClientSide()) {
				level.levelEvent(2001, pos, Block.getId(block));
			} else {
				level.levelEvent(null, 2001, pos, Block.getId(block));
			}
		}

		if (sound) {
			SoundType soundType = block.getSoundType(level, pos, null);

			level.playSound(
					null,
					pos,
					soundType.getBreakSound(),
					SoundSource.BLOCKS,
					(soundType.getVolume() + 1.0F) / 2.0F,
					soundType.getPitch() * 0.8F
			);
		}
	}
}