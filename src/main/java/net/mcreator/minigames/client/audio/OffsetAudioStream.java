package net.mcreator.minigames.client.audio;

import net.minecraft.client.sounds.AudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;

public class OffsetAudioStream implements AudioStream {
	private final AudioStream original;
	private final long bytesToSkip;
	private final long maxBytesToDeliver;
	private long bytesDelivered = 0;
	private boolean skipped = false;

	public OffsetAudioStream(AudioStream original, double skipSeconds, double maxPlaySeconds) {
		this.original = original;
		AudioFormat format = original.getFormat();
		int channels = Math.max(1, format.getChannels());
		int sampleSizeInBytes = Math.max(1, format.getSampleSizeInBits() / 8);
		int frameSize = format.getFrameSize();
		if (frameSize <= 0) {
			frameSize = channels * sampleSizeInBytes;
		}
		float sampleRate = format.getSampleRate();
		long bytesPerSec = (long) (sampleRate * frameSize);

		long totalSkip = (long) (skipSeconds * bytesPerSec);
		if (frameSize > 0) {
			totalSkip = (totalSkip / frameSize) * frameSize;
		}
		this.bytesToSkip = Math.max(0, totalSkip);

		if (maxPlaySeconds > 0.0) {
			long maxBytes = (long) (maxPlaySeconds * bytesPerSec);
			if (frameSize > 0) {
				maxBytes = (maxBytes / frameSize) * frameSize;
			}
			this.maxBytesToDeliver = maxBytes;
		} else {
			this.maxBytesToDeliver = Long.MAX_VALUE;
		}
	}

	@Override
	public AudioFormat getFormat() {
		return original.getFormat();
	}

	@Override
	public ByteBuffer read(int size) throws IOException {
		if (bytesDelivered >= maxBytesToDeliver) {
			return null;
		}

		if (!skipped) {
			skipped = true;
			long remaining = bytesToSkip;
			while (remaining > 0) {
				int toRead = (int) Math.min(remaining, 65536);
				ByteBuffer buf = original.read(toRead);
				if (buf == null || !buf.hasRemaining()) {
					break;
				}
				remaining -= buf.remaining();
			}
		}

		long remainingAllowed = maxBytesToDeliver - bytesDelivered;
		int actualToRead = (int) Math.min(size, remainingAllowed);
		if (actualToRead <= 0) {
			return null;
		}

		ByteBuffer result = original.read(actualToRead);
		if (result != null) {
			bytesDelivered += result.remaining();
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		original.close();
	}
}
