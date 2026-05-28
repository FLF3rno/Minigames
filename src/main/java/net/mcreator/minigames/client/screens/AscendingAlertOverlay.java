package net.mcreator.minigames.client.screens;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(Dist.CLIENT)
public class AscendingAlertOverlay {
	private static float smoothedPercent = 0f;

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Player local = mc.player;
		if (local == null || local.level() == null)
			return;

		Player ascendingPlayer = null;
		for (Player player : local.level().players()) {
			if (player != null && player.hasEffect(MinigamesModMobEffects.ASCENDING)) {
				ascendingPlayer = player;
				break;
			}
		}
		if (ascendingPlayer == null) {
			smoothedPercent = 0f;
			return;
		}

		double rawTimer = ascendingPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).ascendingTimer;
		float targetPercent = Mth.clamp((float) (rawTimer / 350.0), 0f, 1f);
		smoothedPercent = Mth.lerp(0.18f, smoothedPercent, targetPercent);
		if (Math.abs(smoothedPercent - targetPercent) < 0.001f) {
			smoothedPercent = targetPercent;
		}

		int w = event.getGuiGraphics().guiWidth();
		int barY = 0;
		int barHeight = 10;
		int bgColor = 0x44000000;
		int fillColor = colorFromPlayer(ascendingPlayer);

		event.getGuiGraphics().fill(0, barY, w, barY + barHeight, bgColor);
		event.getGuiGraphics().fill(0, barY, Math.max(1, (int) (w * smoothedPercent)), barY + barHeight, fillColor);

		String playerName = ascendingPlayer.getGameProfile().getName();
		Component lineStart = Component.literal(playerName).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(fillColor & 0x00FFFFFF)));
		int whitePulse = pulseWhite(local.tickCount, event.getPartialTick().getGameTimeDeltaPartialTick(false));
		int pulsedTextColor = mixWithWhite(0xFFD54A, whitePulse);
		Component lineEnd = Component.literal(" is ascending! Hit them quick!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(pulsedTextColor & 0x00FFFFFF)));
		Component message = Component.empty().append(lineStart).append(lineEnd);

		int textY = barY + barHeight + 2;
		int messageWidth = mc.font.width(message);
		int x = (w - messageWidth) / 2;
		int nameWidth = mc.font.width(playerName);
		event.getGuiGraphics().drawString(mc.font, lineStart, x, textY, fillColor, false);
		event.getGuiGraphics().drawString(mc.font, lineEnd, x + nameWidth, textY, pulsedTextColor, false);
	}

	private static int colorFromPlayer(Player player) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || vars.color == null || vars.color.isEmpty()) {
			return 0xFFFF5555;
		}
		String hex = vars.color.startsWith("#") ? vars.color.substring(1) : vars.color;
		try {
			int rgb = Integer.parseInt(hex, 16) & 0x00FFFFFF;
			return 0xFF000000 | rgb;
		} catch (Exception ignored) {
			return 0xFFFF5555;
		}
	}

	private static int pulseWhite(int tickCount, float partialTick) {
		return (int) (Math.sin((tickCount + partialTick) * 0.3f) * 60.0f + 195.0f);
	}

	private static int mixWithWhite(int color, int whitePulse) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		float t = (whitePulse - 195.0f) / 60.0f;
		t = Mth.clamp(t, 0.0f, 1.0f);
		int outR = (int) Mth.lerp(t, r, 255.0f);
		int outG = (int) Mth.lerp(t, g, 255.0f);
		int outB = (int) Mth.lerp(t, b, 255.0f);
		return 0xFF000000 | (outR << 16) | (outG << 8) | outB;
	}
}
