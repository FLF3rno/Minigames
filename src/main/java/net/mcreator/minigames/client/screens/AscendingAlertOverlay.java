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
		if (local == null || local.level() == null || local.hasEffect(MinigamesModMobEffects.ASCENDING))
			return;

		Player ascendingPlayer = null;
		for (Player player : local.level().players()) {
			if (player != null && player.getData(MinigamesModVariables.PLAYER_VARIABLES).ascendingActive) {
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
		int barY = 2;
		int barHeight = 10;
		int borderColor = 0xAA000000;
		int bgColor = 0x66000000;
		int fillColor = colorFromPlayer(ascendingPlayer);

		event.getGuiGraphics().fill(0, barY, w, barY + barHeight, bgColor);
		event.getGuiGraphics().fill(0, barY, Math.max(1, (int) (w * smoothedPercent)), barY + barHeight, fillColor);
		event.getGuiGraphics().fill(0, barY - 1, w, barY, borderColor);
		event.getGuiGraphics().fill(0, barY + barHeight, w, barY + barHeight + 1, borderColor);

		String playerName = ascendingPlayer.getGameProfile().getName();
		Component lineStart = Component.literal(playerName).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(fillColor & 0x00FFFFFF)));
		Component lineEnd = Component.literal(" is ascending! Hit them quick!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFD54A)));
		Component message = Component.empty().append(lineStart).append(lineEnd);

		int pulse = (int) (Math.sin((local.tickCount + event.getPartialTick().getGameTimeDeltaPartialTick(false)) * 0.3f) * 60.0f + 195.0f);
		int glow = (pulse << 16) | (pulse << 8) | pulse;
		int textY = barY + barHeight + 3;
		int messageWidth = mc.font.width(message);
		int x = (w - messageWidth) / 2;
		event.getGuiGraphics().drawString(mc.font, message, x + 1, textY + 1, 0xAA000000, false);
		event.getGuiGraphics().drawString(mc.font, message, x, textY, glow, false);
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
}
