package net.mcreator.minigames.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import com.mojang.blaze3d.audio.Channel;
import com.google.gson.Gson;

import net.mcreator.minigames.item.BeaterItem;
import net.mcreator.minigames.network.BeaterMissMessage;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.google.gson.GsonBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@EventBusSubscriber(value = Dist.CLIENT)
public class BeaterMusicManager {

	// Timing calibration constants
	public static final double AUDIO_LATENCY_COMPENSATION = 0.060; // 60ms audio driver delay compensation
	public static final double ON_BEAT_WINDOW = 0.135; // +/- 135ms musical beat window
	public static final double OFF_BEAT_WINDOW = 0.240; // +/- 240ms early/late window

	public static class ChartData {
		public String name;
		public String sound;
		public double bpm;
		public double skipIntroSeconds;
		public double skipOutroSeconds;
		public double songLengthSeconds;
		public double endSeconds;
		public String description;
		public List<Double> beats;
	}

	public record Track(
			String name,
			String chartId,
			Identifier soundId,
			double bpm,
			double skipIntroSeconds,
			double skipOutroSeconds,
			double songLengthSeconds,
			double endSeconds,
			double firstBeatOffset,
			double[] customBeats
	) {
		public double getBeatInterval() {
			return 60.0 / bpm;
		}

		public SoundEvent getSoundEvent() {
			SoundEvent event = BuiltInRegistries.SOUND_EVENT.getValue(soundId);
			if (event != null) {
				return event;
			}
			return SoundEvents.MUSIC_DISC_PIGSTEP.value();
		}

		public double getMaxDurationSeconds() {
			if (endSeconds > 0.0) {
				return endSeconds;
			}
			if (songLengthSeconds > 0.0 && skipOutroSeconds > 0.0) {
				return Math.max(1.0, (songLengthSeconds - skipIntroSeconds) - skipOutroSeconds);
			}
			return -1.0;
		}
	}

	private static ChartData readChartData(String chartId) {
		try (InputStream in = BeaterMusicManager.class.getResourceAsStream("/assets/minigames/charts/" + chartId + ".json")) {
			if (in != null) {
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				return new Gson().fromJson(reader, ChartData.class);
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	private static Track createTrack(String name, String chartId, Identifier soundId, double defaultBpm, double defaultIntro, double defaultOutro, double defaultLength) {
		ChartData data = readChartData(chartId);
		double bpm = (data != null && data.bpm > 0) ? data.bpm : defaultBpm;
		double skipIntro = (data != null && data.skipIntroSeconds > 0) ? data.skipIntroSeconds : defaultIntro;
		double skipOutro = (data != null && data.skipOutroSeconds > 0) ? data.skipOutroSeconds : defaultOutro;
		double songLength = (data != null && data.songLengthSeconds > 0) ? data.songLengthSeconds : defaultLength;
		double endSec = data != null ? data.endSeconds : 0.0;
		double[] beats = null;
		if (data != null && data.beats != null && !data.beats.isEmpty()) {
			beats = new double[data.beats.size()];
			for (int i = 0; i < data.beats.size(); i++) {
				beats[i] = data.beats.get(i);
			}
			Arrays.sort(beats);
		}
		return new Track(name, chartId, soundId, bpm, skipIntro, skipOutro, songLength, endSec, 0.0, beats);
	}

	private static List<Track> loadTracks() {
		List<Track> list = new ArrayList<>();
		list.add(createTrack("Pigstep", "pigstep", Identifier.parse("minecraft:music_disc.pigstep"), 85.0, 11.490, 12.0, 148.8));
		list.add(createTrack("Otherside", "otherside", Identifier.parse("minecraft:music_disc.otherside"), 92.0, 20.870, 15.0, 195.2));
		ChartData creatorData = readChartData("creator");
		if (creatorData != null && creatorData.beats != null && !creatorData.beats.isEmpty()) {
			list.add(createTrack("Creator", "creator", Identifier.parse("minecraft:music_disc.creator"), 81.0, 0.0, 0.0, 176.0));
		}
		return list;
	}

	private static List<Track> tracks = null;
	private static final List<Integer> PLAYLIST = new ArrayList<>();
	private static int playlistIndex = 0;

	private static BeaterSoundInstance currentSound = null;
	private static String lastRating = null;
	private static long lastRatingTime = 0;
	private static long lastOnBeatHitTime = 0;

	// Audio clock & synchronization
	private static boolean isAudioPlaying = false;
	private static double audioElapsedSeconds = 0.0;
	private static long lastFrameNanos = 0;

	// Mixin intro/outro cutting flags
	private static boolean startingBeaterSound = false;
	private static double pendingSkipSeconds = 0.0;
	private static double pendingMaxPlaySeconds = -1.0;

	// Beat miss detection ("WAKE UP!")
	private static long lastEvaluatedBeat = -1;
	private static long lastClickedBeat = -1;

	// Recording state
	private static boolean isRecording = false;
	private static String recordingSongName = "";
	private static String recordingChartId = "";
	private static Identifier recordingSoundId = null;
	private static double recordingBpm = 100.0;
	private static double recordingSkipIntro = 0.0;
	private static double recordingSkipOutro = 0.0;
	private static double recordingSongLength = 0.0;
	private static final List<Double> recordedBeats = new ArrayList<>();
	private static long lastRecordedClickMs = 0;

	public static class BeaterSoundInstance extends AbstractTickableSoundInstance {
		private final Track track;
		private boolean isHeld = false;
		private Channel channel = null;

		public BeaterSoundInstance(Track track) {
			super(track.getSoundEvent(), SoundSource.RECORDS, SoundInstance.createUnseededRandom());
			this.track = track;
			this.looping = false;
			this.delay = 0;
			this.relative = true;
			this.attenuation = Attenuation.NONE;
			this.volume = 1.0f;
			this.pitch = 1.0f;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void tick() {
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null) {
				this.stop();
				return;
			}

			boolean holding = mc.player.getMainHandItem().getItem() instanceof BeaterItem
					|| mc.player.getOffhandItem().getItem() instanceof BeaterItem;
			this.isHeld = holding;

			if (holding || isRecording) {
				float master = mc.options.getSoundSourceVolume(SoundSource.MASTER);
				float records = mc.options.getSoundSourceVolume(SoundSource.RECORDS);
				// Louder: 1.55f volume scale so music punches through combat sounds
				this.volume = Math.max(0.001f, master * records * 1.55f);
			} else {
				// Inaudible but continues playing in real-time in the background!
				this.volume = 0.00001f;
			}

			if (this.channel != null && !this.isStopped()) {
				this.channel.setVolume(this.volume);
			}
		}

		public Track getTrack() {
			return track;
		}

		public boolean isHeld() {
			return isHeld;
		}

		public void stopSound() {
			this.stop();
		}
	}

	public static class TimingInfo {
		public final double phase; // [0, tBeat) in seconds
		public final double delta; // distance to nearest beat in seconds
		public final double tBeat;
		public final boolean inBeatWindow;
		public final String rating;
		public final boolean onBeat;
		public final double timeSinceBeat0;
		public final long nearestBeatIndex;

		public TimingInfo(double phase, double delta, double tBeat, boolean inBeatWindow, String rating, boolean onBeat, double timeSinceBeat0, long nearestBeatIndex) {
			this.phase = phase;
			this.delta = delta;
			this.tBeat = tBeat;
			this.inBeatWindow = inBeatWindow;
			this.rating = rating;
			this.onBeat = onBeat;
			this.timeSinceBeat0 = timeSinceBeat0;
			this.nearestBeatIndex = nearestBeatIndex;
		}
	}

	private static void updateAudioClock() {
		Minecraft mc = Minecraft.getInstance();
		long now = System.nanoTime();
		if (lastFrameNanos == 0) {
			lastFrameNanos = now;
			return;
		}

		long deltaNanos = now - lastFrameNanos;
		lastFrameNanos = now;

		// FREEZE audio clock during pause so pausing NEVER desyncs the rhythm!
		if (mc.isPaused()) {
			return;
		}

		if (isAudioPlaying && currentSound != null && !currentSound.isStopped()) {
			if (deltaNanos > 0 && deltaNanos < 500_000_000L) {
				audioElapsedSeconds += (deltaNanos / 1_000_000_000.0);
			}
		}
	}

	public static TimingInfo getTimingInfo() {
		updateAudioClock();

		if (currentSound == null || currentSound.isStopped() || !isAudioPlaying) {
			return null;
		}

		Track track = currentSound.getTrack();
		double tBeat = track.getBeatInterval();
		double elapsed = audioElapsedSeconds;
		// Compensate for audio driver output latency so what player HEARS matches exactly when to click!
		double timeSinceBeat0 = elapsed - track.firstBeatOffset() - AUDIO_LATENCY_COMPENSATION;

		double nearestBeatTime;
		long nearestBeatIndex;
		double phase;

		double[] beats = track.customBeats();
		if (beats != null && beats.length > 0) {
			int idx = Arrays.binarySearch(beats, timeSinceBeat0);
			int insertionPoint = idx >= 0 ? idx : (-idx - 1);

			int closestIdx;
			if (insertionPoint == 0) {
				closestIdx = 0;
			} else if (insertionPoint >= beats.length) {
				closestIdx = beats.length - 1;
			} else {
				double distBefore = Math.abs(timeSinceBeat0 - beats[insertionPoint - 1]);
				double distAfter = Math.abs(timeSinceBeat0 - beats[insertionPoint]);
				closestIdx = (distBefore <= distAfter) ? (insertionPoint - 1) : insertionPoint;
			}

			nearestBeatIndex = closestIdx;
			nearestBeatTime = beats[closestIdx];

			if (closestIdx + 1 < beats.length && timeSinceBeat0 >= nearestBeatTime) {
				phase = timeSinceBeat0 - nearestBeatTime;
			} else if (closestIdx > 0 && timeSinceBeat0 < nearestBeatTime) {
				phase = timeSinceBeat0 - beats[closestIdx - 1];
			} else {
				phase = 0.0;
			}
		} else {
			// Fallback to mathematical BPM interval
			nearestBeatIndex = Math.round(timeSinceBeat0 / tBeat);
			nearestBeatTime = nearestBeatIndex * tBeat;
			phase = timeSinceBeat0 % tBeat;
			if (phase < 0) {
				phase += tBeat;
			}
		}

		double delta = timeSinceBeat0 - nearestBeatTime;
		double absDelta = Math.abs(delta);

		// Active beat window (flashes white during this time): +/- 0.135s
		boolean inBeatWindow = absDelta <= ON_BEAT_WINDOW;

		String rating;
		boolean onBeat;
		if (absDelta <= ON_BEAT_WINDOW) {
			rating = "ON BEAT";
			onBeat = true;
		} else if (delta < -ON_BEAT_WINDOW && delta >= -OFF_BEAT_WINDOW) {
			rating = "EARLY";
			onBeat = false;
		} else if (delta > ON_BEAT_WINDOW && delta <= OFF_BEAT_WINDOW) {
			rating = "LATE";
			onBeat = false;
		} else {
			rating = "???";
			onBeat = false;
		}

		return new TimingInfo(phase, delta, tBeat, inBeatWindow, rating, onBeat, timeSinceBeat0, nearestBeatIndex);
	}

	private static void playNextTrack() {
		if (tracks == null) {
			tracks = loadTracks();
		}

		if (PLAYLIST.isEmpty() || playlistIndex >= PLAYLIST.size()) {
			PLAYLIST.clear();
			for (int i = 0; i < tracks.size(); i++) {
				PLAYLIST.add(i);
			}
			Collections.shuffle(PLAYLIST);
			playlistIndex = 0;
		}

		Track nextTrack = tracks.get(PLAYLIST.get(playlistIndex++));
		currentSound = new BeaterSoundInstance(nextTrack);
		isAudioPlaying = false;
		audioElapsedSeconds = 0.0;
		lastFrameNanos = System.nanoTime();
		lastEvaluatedBeat = -1;
		lastClickedBeat = -1;

		startingBeaterSound = true;
		pendingSkipSeconds = nextTrack.skipIntroSeconds();
		pendingMaxPlaySeconds = nextTrack.getMaxDurationSeconds();
		try {
			Minecraft.getInstance().getSoundManager().play(currentSound);
		} finally {
			startingBeaterSound = false;
		}
	}

	@SubscribeEvent
	public static void onPlayStreamingSource(PlayStreamingSourceEvent event) {
		if (event.getSound() == currentSound || event.getSound() instanceof BeaterSoundInstance) {
			isAudioPlaying = true;
			audioElapsedSeconds = 0.0;
			lastFrameNanos = System.nanoTime();
			lastEvaluatedBeat = -1;
			lastClickedBeat = -1;
			if (currentSound != null) {
				currentSound.setChannel(event.getChannel());
			}
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			if (currentSound != null) {
				currentSound = null;
				isAudioPlaying = false;
			}
			return;
		}

		updateAudioClock();

		if (isRecording) {
			if (isAudioPlaying && currentSound != null) {
				double maxDur = currentSound.getTrack().getMaxDurationSeconds();
				if (maxDur > 0.0 && audioElapsedSeconds >= maxDur) {
					saveCurrentRecording();
				}
			}
			return;
		}

		boolean holding = mc.player.getMainHandItem().getItem() instanceof BeaterItem
				|| mc.player.getOffhandItem().getItem() instanceof BeaterItem;

		if (currentSound == null) {
			if (holding) {
				playNextTrack();
			}
			return;
		} else {
			if (currentSound.isStopped() || !mc.getSoundManager().isActive(currentSound)) {
				playNextTrack();
				return;
			}
		}

		// Outro cutoff check: seamlessly transition to next track when skipOutroSeconds is reached
		if (isAudioPlaying) {
			double maxDur = currentSound.getTrack().getMaxDurationSeconds();
			if (maxDur > 0.0 && audioElapsedSeconds >= maxDur) {
				mc.getSoundManager().stop(currentSound);
				currentSound.stopSound();
				playNextTrack();
				return;
			}
		}

		// Check for missed beats ("WAKE UP!") when holding weapon
		if (!holding || !isAudioPlaying || mc.isPaused()) {
			lastEvaluatedBeat = -1;
			lastClickedBeat = -1;
			return;
		}

		TimingInfo timing = getTimingInfo();
		if (timing == null || timing.timeSinceBeat0 < 0) {
			return;
		}

		if (lastEvaluatedBeat == -1) {
			lastEvaluatedBeat = timing.nearestBeatIndex;
			lastClickedBeat = timing.nearestBeatIndex;
			return;
		}

		Track track = currentSound.getTrack();
		double[] beats = track.customBeats();
		if (beats != null && beats.length > 0) {
			while (lastEvaluatedBeat < beats.length) {
				int bIdx = (int) lastEvaluatedBeat;
				double targetBeatTime = beats[bIdx];
				double expiry = targetBeatTime + OFF_BEAT_WINDOW;
				if (timing.timeSinceBeat0 > expiry) {
					if (lastClickedBeat < lastEvaluatedBeat) {
						triggerRating("WAKE UP!");
						ClientPacketDistributor.sendToServer(new BeaterMissMessage());
						lastClickedBeat = lastEvaluatedBeat;
					}
					lastEvaluatedBeat++;
				} else {
					break;
				}
			}
		} else {
			double beatExpiryTime = (lastEvaluatedBeat * timing.tBeat) + OFF_BEAT_WINDOW;
			while (timing.timeSinceBeat0 > beatExpiryTime) {
				if (lastClickedBeat < lastEvaluatedBeat) {
					triggerRating("WAKE UP!");
					ClientPacketDistributor.sendToServer(new BeaterMissMessage());
					lastClickedBeat = lastEvaluatedBeat;
				}
				lastEvaluatedBeat++;
				beatExpiryTime = (lastEvaluatedBeat * timing.tBeat) + ON_BEAT_WINDOW + 0.04;
			}
		}
	}

	public static void recordBeatClicked(long beatIndex) {
		lastClickedBeat = Math.max(lastClickedBeat, beatIndex);
	}

	public static void triggerRating(String rating) {
		lastRating = rating;
		lastRatingTime = System.currentTimeMillis();
	}

	public static void triggerOnBeatHit() {
		lastOnBeatHitTime = System.currentTimeMillis();
	}

	public static long getLastOnBeatHitTime() {
		return lastOnBeatHitTime;
	}

	public static String getLastRating() {
		return lastRating;
	}

	public static long getLastRatingTime() {
		return lastRatingTime;
	}

	public static boolean isStartingBeaterSound() {
		return startingBeaterSound;
	}

	public static double getPendingSkipSeconds() {
		return pendingSkipSeconds;
	}

	public static double getPendingMaxPlaySeconds() {
		return pendingMaxPlaySeconds;
	}

	// ==================== BEATER RECORDER SYSTEM ====================

	public static boolean isRecording() {
		return isRecording;
	}

	public static String getRecordingTrackName() {
		return recordingSongName;
	}

	public static int getRecordedBeatsCount() {
		return recordedBeats.size();
	}

	public static double getAudioElapsedSeconds() {
		updateAudioClock();
		return audioElapsedSeconds;
	}

	public static void startRecording(String songName, double intro, double outro) {
		String chartId = songName.toLowerCase(Locale.ROOT).trim();
		if (chartId.startsWith("music_disc.")) {
			chartId = chartId.substring("music_disc.".length());
		}
		if (chartId.contains(":")) {
			chartId = chartId.substring(chartId.indexOf(':') + 1);
		}

		ChartData existing = readChartData(chartId);
		double bpm = (existing != null && existing.bpm > 0) ? existing.bpm : 100.0;
		double length = (existing != null && existing.songLengthSeconds > 0) ? existing.songLengthSeconds : getDefaultSongLength(chartId);
		double skipIntro = intro > 0 ? intro : (existing != null && existing.skipIntroSeconds > 0 ? existing.skipIntroSeconds : 0.0);
		double skipOutro = outro > 0 ? outro : (existing != null && existing.skipOutroSeconds > 0 ? existing.skipOutroSeconds : 0.0);

		String displayName = (existing != null && existing.name != null && !existing.name.isEmpty())
				? existing.name
				: (Character.toUpperCase(chartId.charAt(0)) + chartId.substring(1));

		Identifier soundId = (existing != null && existing.sound != null && !existing.sound.isEmpty())
				? Identifier.parse(existing.sound)
				: Identifier.parse("minecraft:music_disc." + chartId);

		isRecording = true;
		recordingSongName = displayName;
		recordingChartId = chartId;
		recordingSoundId = soundId;
		recordingBpm = bpm;
		recordingSkipIntro = skipIntro;
		recordingSkipOutro = skipOutro;
		recordingSongLength = length;
		recordedBeats.clear();
		lastRecordedClickMs = 0;

		Minecraft mc = Minecraft.getInstance();
		if (currentSound != null) {
			mc.getSoundManager().stop(currentSound);
			currentSound = null;
		}

		Track recTrack = new Track(displayName, chartId, soundId, bpm, skipIntro, skipOutro, length, 0.0, 0.0, null);
		currentSound = new BeaterSoundInstance(recTrack);
		isAudioPlaying = false;
		audioElapsedSeconds = 0.0;
		lastFrameNanos = System.nanoTime();
		lastEvaluatedBeat = -1;
		lastClickedBeat = -1;

		startingBeaterSound = true;
		pendingSkipSeconds = skipIntro;
		pendingMaxPlaySeconds = recTrack.getMaxDurationSeconds();
		try {
			mc.getSoundManager().play(currentSound);
		} finally {
			startingBeaterSound = false;
		}
	}

	public static void recordCurrentBeat() {
		if (!isRecording || !isAudioPlaying || currentSound == null) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.isPaused() || mc.player == null) {
			return;
		}

		long now = System.currentTimeMillis();
		if (now - lastRecordedClickMs < 120) {
			return;
		}
		lastRecordedClickMs = now;

		updateAudioClock();

		double time = Math.max(0.0, audioElapsedSeconds - AUDIO_LATENCY_COMPENSATION);
		double roundedTime = Math.round(time * 1000.0) / 1000.0;

		recordedBeats.add(roundedTime);

		mc.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.8f, 1.8f);
		if (mc.gui != null) {
			mc.gui.setOverlayMessage(
					Component.literal("§a[REC Beat #" + recordedBeats.size() + "] §e" + String.format(Locale.US, "%.3fs", roundedTime)),
					false
			);
		}
	}

	public static void clearRecording() {
		recordedBeats.clear();
		lastRecordedClickMs = 0;
		if (isRecording && !recordingChartId.isEmpty()) {
			startRecording(recordingChartId, recordingSkipIntro, recordingSkipOutro);
		}
	}

	public static void saveCurrentRecording() {
		if (!isRecording) {
			return;
		}
		isRecording = false;

		Minecraft mc = Minecraft.getInstance();
		if (currentSound != null) {
			mc.getSoundManager().stop(currentSound);
			currentSound = null;
		}
		isAudioPlaying = false;

		if (recordedBeats.isEmpty()) {
			if (mc.player != null) {
				mc.player.sendSystemMessage(Component.literal("§c[Beater Recorder] No beats were recorded. Nothing saved.").withStyle(ChatFormatting.RED));
			}
			return;
		}

		Collections.sort(recordedBeats);
		List<Double> cleanBeats = new ArrayList<>();
		for (double b : recordedBeats) {
			if (cleanBeats.isEmpty() || Math.abs(b - cleanBeats.get(cleanBeats.size() - 1)) > 0.05) {
				cleanBeats.add(b);
			}
		}

		double calculatedBpm = recordingBpm;
		if (cleanBeats.size() >= 4) {
			double totalInterval = 0.0;
			int intervalsCount = 0;
			for (int i = 1; i < cleanBeats.size(); i++) {
				double diff = cleanBeats.get(i) - cleanBeats.get(i - 1);
				if (diff > 0.2 && diff < 2.0) {
					totalInterval += diff;
					intervalsCount++;
				}
			}
			if (intervalsCount > 0) {
				double avgInterval = totalInterval / intervalsCount;
				double estBpm = Math.round((60.0 / avgInterval) * 10.0) / 10.0;
				if (estBpm >= 40.0 && estBpm <= 220.0) {
					calculatedBpm = estBpm;
				}
			}
		}

		ChartData chart = new ChartData();
		chart.name = recordingSongName;
		chart.sound = recordingSoundId.toString();
		chart.bpm = calculatedBpm;
		chart.songLengthSeconds = recordingSongLength;
		chart.skipIntroSeconds = recordingSkipIntro;
		chart.skipOutroSeconds = recordingSkipOutro;
		chart.description = "Chart for " + recordingSongName + " disc. Auto-generated by Beater Recorder.";
		chart.beats = cleanBeats;

		String json = new GsonBuilder().setPrettyPrinting().create().toJson(chart);

		File targetFile = resolveChartFile(recordingChartId);
		boolean saved = false;
		try {
			if (targetFile != null) {
				File parent = targetFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				Files.writeString(targetFile.toPath(), json, StandardCharsets.UTF_8);
				saved = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (saved && mc.player != null) {
			mc.player.sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
			mc.player.sendSystemMessage(Component.literal("§a[Beater Recorder] Successfully saved §e" + cleanBeats.size() + "§a beats for §e" + recordingSongName + "§a!").withStyle(ChatFormatting.GREEN));
			mc.player.sendSystemMessage(Component.literal("§7BPM: §f" + calculatedBpm + "§7 | Intro Cut: §f" + recordingSkipIntro + "s§7 | Outro Cut: §f" + recordingSkipOutro + "s").withStyle(ChatFormatting.GRAY));
			mc.player.sendSystemMessage(Component.literal("§7Saved to: §b" + targetFile.getAbsolutePath()).withStyle(ChatFormatting.AQUA));
			mc.player.sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
		} else if (mc.player != null) {
			mc.player.sendSystemMessage(Component.literal("§c[Beater Recorder] Failed to write chart file for " + recordingChartId).withStyle(ChatFormatting.RED));
		}

		reloadTrack(recordingSongName, recordingChartId, recordingSoundId, calculatedBpm, recordingSkipIntro, recordingSkipOutro, recordingSongLength, cleanBeats);
	}

	private static File resolveChartFile(String chartId) {
		File devSourceFile = new File("c:/Users/User/Desktop/McreatorGithub/Minigames/src/main/resources/assets/minigames/charts/" + chartId + ".json");
		if (devSourceFile.getParentFile() != null && devSourceFile.getParentFile().exists()) {
			return devSourceFile;
		}

		File relativeFile = new File("src/main/resources/assets/minigames/charts/" + chartId + ".json");
		if (relativeFile.getParentFile() != null && relativeFile.getParentFile().exists()) {
			return relativeFile;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.gameDirectory != null) {
			File ideFile = new File(mc.gameDirectory, "../../src/main/resources/assets/minigames/charts/" + chartId + ".json");
			if (ideFile.getParentFile() != null && ideFile.getParentFile().exists()) {
				return ideFile;
			}
			return new File(mc.gameDirectory, "minigames_charts/" + chartId + ".json");
		}

		return devSourceFile;
	}

	private static void reloadTrack(String name, String chartId, Identifier soundId, double bpm, double intro, double outro, double length, List<Double> beatsList) {
		if (tracks == null) {
			tracks = new ArrayList<>(loadTracks());
		} else if (!(tracks instanceof ArrayList)) {
			tracks = new ArrayList<>(tracks);
		}

		double[] beats = new double[beatsList.size()];
		for (int i = 0; i < beatsList.size(); i++) {
			beats[i] = beatsList.get(i);
		}
		Arrays.sort(beats);

		Track newTrack = new Track(name, chartId, soundId, bpm, intro, outro, length, 0.0, 0.0, beats);

		boolean replaced = false;
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i).chartId().equalsIgnoreCase(chartId)) {
				tracks.set(i, newTrack);
				replaced = true;
				break;
			}
		}
		if (!replaced) {
			tracks.add(newTrack);
		}

		PLAYLIST.clear();
		for (int i = 0; i < tracks.size(); i++) {
			PLAYLIST.add(i);
		}
		Collections.shuffle(PLAYLIST);
		playlistIndex = 0;
	}

	private static double getDefaultSongLength(String chartId) {
		return switch (chartId.toLowerCase(Locale.ROOT)) {
			case "pigstep" -> 148.8;
			case "otherside" -> 195.2;
			case "creator" -> 176.0;
			case "creator_music_box" -> 73.0;
			case "precipice" -> 299.0;
			case "relic" -> 218.0;
			case "5" -> 178.0;
			case "cat" -> 185.0;
			case "blocks" -> 345.0;
			case "chirp" -> 185.0;
			case "far" -> 194.0;
			case "mall" -> 197.0;
			case "mellohi" -> 96.0;
			case "stal" -> 150.0;
			case "strad" -> 188.0;
			case "ward" -> 251.0;
			case "11" -> 71.0;
			case "13" -> 178.0;
			case "wait" -> 238.0;
			default -> 180.0;
		};
	}
}
