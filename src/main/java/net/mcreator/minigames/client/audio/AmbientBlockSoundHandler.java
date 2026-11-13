package net.mcreator.minigames.client.audio;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Queue;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayDeque;

public class AmbientBlockSoundHandler {
	private static final Map<BlockPos, AmbientSoundInstance> ACTIVE_SOUNDS = new ConcurrentHashMap<>();

	public static void playAmbientSound(Level level, BlockPos pos, Block block, SoundEvent sound, int delay, double range, boolean muteConnected) {
		if (level == null || !level.isClientSide() || sound == null || pos == null || block == null) {
			return;
		}

		BlockPos immutablePos = pos.immutable();

		// Already playing at this exact position?
		AmbientSoundInstance existing = ACTIVE_SOUNDS.get(immutablePos);
		if (existing != null && !existing.isStopped()) {
			return;
		}

		// If muting connected blocks: check if another block in the connected mass is already playing
		if (muteConnected && isConnectedClusterAlreadyPlaying(level, immutablePos, block)) {
			return;
		}

		// Create and start the looping sound instance
		AmbientSoundInstance instance = new AmbientSoundInstance(sound, immutablePos, block, delay, range, muteConnected);
		ACTIVE_SOUNDS.put(immutablePos, instance);
		Minecraft.getInstance().getSoundManager().play(instance);
	}

	public static void playAmbientSound(Level level, BlockPos pos, Block block, SoundEvent sound, int delay, boolean muteConnected) {
		playAmbientSound(level, pos, block, sound, delay, 16.0D, muteConnected);
	}

	private static boolean isConnectedClusterAlreadyPlaying(Level level, BlockPos startPos, Block targetBlock) {
		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new ArrayDeque<>();

		queue.add(startPos);
		visited.add(startPos);

		int maxBlocks = 64; // cluster search limit for performance
		while (!queue.isEmpty() && visited.size() <= maxBlocks) {
			BlockPos current = queue.poll();

			for (Direction dir : Direction.values()) {
				BlockPos neighbor = current.relative(dir);
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					if (level.getBlockState(neighbor).is(targetBlock)) {
						AmbientSoundInstance sound = ACTIVE_SOUNDS.get(neighbor);
						if (sound != null && !sound.isStopped()) {
							return true;
						}
						queue.add(neighbor);
					}
				}
			}
		}
		return false;
	}

	public static class AmbientSoundInstance extends AbstractTickableSoundInstance {
		private final BlockPos blockPos;
		private final Block expectedBlock;
		private final boolean muteConnected;

		public AmbientSoundInstance(SoundEvent sound, BlockPos pos, Block block, int delay, double range, boolean muteConnected) {
			super(sound, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
			this.blockPos = pos;
			this.expectedBlock = block;
			this.muteConnected = muteConnected;
			this.x = pos.getX() + 0.5D;
			this.y = pos.getY() + 0.5D;
			this.z = pos.getZ() + 0.5D;
			this.volume = (float) Math.max(0.01, range > 0 ? (range / 16.0) : 1.0);
			this.pitch = 1.0F;
			this.attenuation = Attenuation.LINEAR;
			this.looping = true;
			this.delay = Math.max(0, delay);
		}

		public AmbientSoundInstance(SoundEvent sound, BlockPos pos, Block block, int delay, boolean muteConnected) {
			this(sound, pos, block, delay, 16.0D, muteConnected);
		}

		@Override
		public void tick() {
			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null) {
				stopSound();
				return;
			}

			// Check if chunk is loaded and block still exists
			if (!mc.level.hasChunkAt(this.blockPos)) {
				stopSound();
				return;
			}

			if (!mc.level.getBlockState(this.blockPos).is(this.expectedBlock)) {
				stopSound();
				return;
			}
		}

		private void stopSound() {
			this.stop();
			ACTIVE_SOUNDS.remove(this.blockPos);
		}

		@Override
		public boolean canStartSilent() {
			return true;
		}
	}
}
