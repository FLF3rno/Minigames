package net.mcreator.minigames.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.mcreator.minigames.client.BeaterMusicManager;
import net.mcreator.minigames.item.BeaterItem;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class BeaterScreenOverlay {

	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui || mc.player == null || mc.level == null) {
			return;
		}

		boolean isHolding = mc.player.getMainHandItem().getItem() instanceof BeaterItem
				|| mc.player.getOffhandItem().getItem() instanceof BeaterItem;

		if (!isHolding && !BeaterMusicManager.isRecording()) {
			return;
		}

		GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
		int width = guiGraphics.guiWidth();
		int height = guiGraphics.guiHeight();
		if (width <= 0 || height <= 0) {
			return;
		}

		// Recording HUD indicator
		if (BeaterMusicManager.isRecording()) {
			boolean blink = (System.currentTimeMillis() / 500) % 2 == 0;
			int circleColor = blink ? 0xFFFF3333 : 0xFFAA0000;

			String song = BeaterMusicManager.getRecordingTrackName();
			int count = BeaterMusicManager.getRecordedBeatsCount();
			double elapsedSec = BeaterMusicManager.getAudioElapsedSeconds();

			Component recComp = Component.literal("● REC: " + song + "  |  " + count + " beats  |  " + String.format(java.util.Locale.US, "%.1fs", elapsedSec)).withStyle(ChatFormatting.BOLD);
			int textWidth = mc.font.width(recComp);
			int rx = (width - textWidth) / 2;
			int ry = 14;

			guiGraphics.fill(rx - 8, ry - 4, rx + textWidth + 8, ry + 13, 0xAA000000);
			guiGraphics.text(mc.font, recComp, rx, ry, circleColor, true);

			Component tipComp = Component.literal("Left-Click to tap beat  •  /beaterrecord save to finish").withStyle(ChatFormatting.GRAY);
			int tipWidth = mc.font.width(tipComp);
			guiGraphics.text(mc.font, tipComp, (width - tipWidth) / 2, ry + 16, 0xFFDDDDDD, true);

			if (!isHolding) {
				return;
			}
		}

		BeaterMusicManager.TimingInfo timing = BeaterMusicManager.getTimingInfo();
		if (timing == null) {
			return;
		}

		// 1. Screen border color calculation
		int rgb;
		int edgeAlpha;
		int gradientAlpha;

		if (timing.inBeatWindow) {
			// Flashes white during the active beat timing window
			rgb = 0xFFFFFF;
			edgeAlpha = 240;
			gradientAlpha = 150;
		} else {
			// Starts red at the beginning of the beat and smoothly fades to green while getting closer
			double whiteWindow = BeaterMusicManager.ON_BEAT_WINDOW;
			double cycleStart = whiteWindow;
			double cycleEnd = timing.tBeat - whiteWindow;
			double p = (timing.phase - cycleStart) / Math.max(0.001, cycleEnd - cycleStart);
			p = Mth.clamp(p, 0.0, 1.0);

			// Interpolate from RED (255, 30, 30) to GREEN (30, 240, 30)
			int r = (int) (255 * (1.0 - p) + 30 * p);
			int g = (int) (30 * (1.0 - p) + 240 * p);
			int b = 30;
			rgb = (r << 16) | (g << 8) | b;

			// Gains color as it gets closer to the beat
			edgeAlpha = (int) (95 + 115 * p);
			gradientAlpha = (int) (35 + 75 * p);
		}

		// On-beat hit pulse animation (smooth, rapid rainbow fade & expansion in size)
		long onBeatElapsed = System.currentTimeMillis() - BeaterMusicManager.getLastOnBeatHitTime();
		float hitPulse = 0.0f;
		float pulseProgress = 0.0f;
		if (onBeatElapsed >= 0 && onBeatElapsed < 420) {
			pulseProgress = onBeatElapsed / 420.0f;
			hitPulse = (1.0f - pulseProgress) * (1.0f - pulseProgress);
		}

		int baseDepth = Mth.clamp(Math.min(width, height) / 9, 18, 44);
		int depth = (int) (baseDepth + (hitPulse * 45.0f));
		int edgeThickness = 2 + (int) (hitPulse * 5.0f);

		if (hitPulse > 0.01f) {
			edgeAlpha = Math.min(255, (int) (edgeAlpha + hitPulse * 160));
			gradientAlpha = Math.min(255, (int) (gradientAlpha + hitPulse * 180));

			// Smooth reverse rebound: Purple -> Blue -> Cyan -> Green -> Yellow -> Red
			float hue = 0.82f * (1.0f - pulseProgress);
			int rainbow = getRainbowRgb(hue);
			int rbR = (rainbow >> 16) & 0xFF;
			int rbG = (rainbow >> 8) & 0xFF;
			int rbB = rainbow & 0xFF;

			int baseR = (rgb >> 16) & 0xFF;
			int baseG = (rgb >> 8) & 0xFF;
			int baseB = rgb & 0xFF;

			// Blend from vibrant rainbow to base border color as pulse decays
			int finalR = (int) (rbR * hitPulse + baseR * (1.0f - hitPulse));
			int finalG = (int) (rbG * hitPulse + baseG * (1.0f - hitPulse));
			int finalB = (int) (rbB * hitPulse + baseB * (1.0f - hitPulse));
			rgb = (finalR << 16) | (finalG << 8) | finalB;
		}

		// Draw border vignette around screen edges
		guiGraphics.fill(0, 0, width, edgeThickness, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(0, height - edgeThickness, width, height, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(0, edgeThickness, edgeThickness, height - edgeThickness, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(width - edgeThickness, edgeThickness, width, height - edgeThickness, (edgeAlpha << 24) | rgb);

		guiGraphics.fillGradient(0, 0, width, depth, (gradientAlpha << 24) | rgb, rgb & 0x00FFFFFF);
		guiGraphics.fillGradient(0, height - depth, width, height, rgb & 0x00FFFFFF, (gradientAlpha << 24) | rgb);

		for (int i = 0; i < depth; i++) {
			float factor = 1.0f - ((float) i / depth);
			int a = (int) (gradientAlpha * factor * factor);
			if (a <= 0) {
				continue;
			}
			int col = (a << 24) | rgb;
			guiGraphics.fill(i, 0, i + 1, height, col);
			guiGraphics.fill(width - 1 - i, 0, width - i, height, col);
		}

		// 2. Bold purplish shadowed timing rating text over the item name
		long elapsed = System.currentTimeMillis() - BeaterMusicManager.getLastRatingTime();
		if (elapsed < 1200 && BeaterMusicManager.getLastRating() != null) {
			float progress = elapsed / 1200.0f;
			float alpha = progress < 0.65f ? 1.0f : (1.0f - (progress - 0.65f) / 0.35f);
			int textAlpha = (int) (alpha * 255);
			if (textAlpha > 4) {
				String ratingStr = BeaterMusicManager.getLastRating();
				int textColor;
				if ("WAKE UP!".equals(ratingStr)) {
					textColor = (textAlpha << 24) | 0xFF3555;
				} else if ("ON BEAT".equals(ratingStr)) {
					textColor = (textAlpha << 24) | 0xDF70FF;
				} else if ("???".equals(ratingStr)) {
					textColor = (textAlpha << 24) | 0x9950CC;
				} else {
					textColor = (textAlpha << 24) | 0xD050FF;
				}

				Component comp = Component.literal(ratingStr).withStyle(ChatFormatting.BOLD);
				int textWidth = mc.font.width(comp);
				int x = (width - textWidth) / 2;
				int yOffset = (int) (progress * 7.0f);
				int y = height - 72 - yOffset;

				guiGraphics.text(mc.font, comp, x, y, textColor, true);
			}
		}
	}

	private static int getRainbowRgb(float hue) {
		float h = (hue % 1.0f) * 6.0f;
		if (h < 0) {
			h += 6.0f;
		}
		int sector = (int) h;
		float f = h - sector;
		float q = 1.0f - f;
		float r = 0, g = 0, b = 0;
		switch (sector) {
			case 0 -> { r = 1.0f; g = f; b = 0.0f; }
			case 1 -> { r = q; g = 1.0f; b = 0.0f; }
			case 2 -> { r = 0.0f; g = 1.0f; b = f; }
			case 3 -> { r = 0.0f; g = q; b = 1.0f; }
			case 4 -> { r = f; g = 0.0f; b = 1.0f; }
			case 5 -> { r = 1.0f; g = 0.0f; b = q; }
		}
		int ri = (int) (r * 255.0f);
		int gi = (int) (g * 255.0f);
		int bi = (int) (b * 255.0f);
		return (ri << 16) | (gi << 8) | bi;
	}
}
