package net.mcreator.minigames.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.minigames.network.MinigamesModVariables;

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
			if (level instanceof ServerLevel serverLevel) {
				if (MinigamesModVariables.MapVariables.get(serverLevel).playingSpleef) {
					BlockParticleOption particleData = new BlockParticleOption(ParticleTypes.BLOCK, block);
					for (ServerPlayer player : serverLevel.players()) {
						if (player.distanceToSqr(x + 0.5, y + 0.5, z + 0.5) <= 4096.0) {
							if (player.getData(MinigamesModVariables.PLAYER_VARIABLES).ShowSpleefBlockParticles) {
								serverLevel.sendParticles(
										player,
										particleData,
										false,
										false,
										x + 0.5,
										y + 0.5,
										z + 0.5,
										35,
										0.25,
										0.25,
										0.25,
										0.15
								);
							}
						}
					}
				} else {
					level.levelEvent(2001, pos, Block.getId(block));
				}
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