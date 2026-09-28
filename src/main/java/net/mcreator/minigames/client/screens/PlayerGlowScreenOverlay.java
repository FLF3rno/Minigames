package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.client.ClientGlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class PlayerGlowScreenOverlay {

	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui || mc.player == null || mc.level == null) {
			return;
		}

		if (!ClientGlowManager.hasExplicitGlow(mc.player.getId())) {
			return;
		}

		int color = ClientGlowManager.getGlowColor(mc.player);
		if (color == -1) {
			return;
		}

		GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
		int width = guiGraphics.guiWidth();
		int height = guiGraphics.guiHeight();
		if (width <= 0 || height <= 0) {
			return;
		}

		float time = (mc.player.tickCount + event.getPartialTick().getGameTimeDeltaPartialTick(false)) * 0.12f;
		float pulse = 0.82f + 0.18f * Mth.sin(time);

		int edgeAlpha = (int) (180 * pulse);
		int gradientAlpha = (int) (110 * pulse);
		int rgb = color & 0xFFFFFF;

		int depth = Mth.clamp(Math.min(width, height) / 7, 24, 64);

		guiGraphics.fill(0, 0, width, 2, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(0, height - 2, width, height, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(0, 2, 2, height - 2, (edgeAlpha << 24) | rgb);
		guiGraphics.fill(width - 2, 2, width, height - 2, (edgeAlpha << 24) | rgb);

		guiGraphics.fillGradient(0, 0, width, depth, (gradientAlpha << 24) | rgb, rgb);
		guiGraphics.fillGradient(0, height - depth, width, height, rgb, (gradientAlpha << 24) | rgb);

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
	}
}
