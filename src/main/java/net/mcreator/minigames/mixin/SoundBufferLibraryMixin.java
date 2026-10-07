package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.client.BeaterMusicManager;
import net.mcreator.minigames.client.audio.OffsetAudioStream;

import java.util.concurrent.CompletableFuture;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {

	@Inject(method = "getStream", at = @At("RETURN"), cancellable = true)
	private void minigames$modifyAudioStream(Identifier soundLocation, boolean isLooping, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
		if (BeaterMusicManager.isStartingBeaterSound()) {
			double skipSeconds = BeaterMusicManager.getPendingSkipSeconds();
			double maxPlaySeconds = BeaterMusicManager.getPendingMaxPlaySeconds();
			if (skipSeconds > 0.0 || maxPlaySeconds > 0.0) {
				CompletableFuture<AudioStream> wrapped = cir.getReturnValue().thenApply(stream -> new OffsetAudioStream(stream, skipSeconds, maxPlaySeconds));
				cir.setReturnValue(wrapped);
			}
		}
	}
}
