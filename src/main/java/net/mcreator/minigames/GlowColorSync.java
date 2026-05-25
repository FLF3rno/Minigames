package net.mcreator.minigames;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(modid = MinigamesMod.MODID)
public class GlowColorSync {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;
		if (!player.hasEffect(net.minecraft.world.effect.MobEffects.GLOWING))
			return;
		// Do not assign color glow teams to players.
	}

	public static void applyGlowTeam(Entity entity, String hexColor) {
		if (entity == null || entity.level().isClientSide())
			return;
		// Do not modify scoreboard teams for glow application.
		if (entity instanceof LivingEntity living) {
			living.setGlowingTag(true);
		}
	}

	private static ChatFormatting nearestFormatting(String hexColor) {
		int rgb = parseHex(hexColor, 0xFFFFFF);
		int r = (rgb >> 16) & 255;
		int g = (rgb >> 8) & 255;
		int b = rgb & 255;
		ChatFormatting[] palette = new ChatFormatting[] {
			ChatFormatting.BLACK, ChatFormatting.DARK_BLUE, ChatFormatting.DARK_GREEN, ChatFormatting.DARK_AQUA, ChatFormatting.DARK_RED, ChatFormatting.DARK_PURPLE, ChatFormatting.GOLD, ChatFormatting.GRAY,
			ChatFormatting.DARK_GRAY, ChatFormatting.BLUE, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.RED, ChatFormatting.LIGHT_PURPLE, ChatFormatting.YELLOW, ChatFormatting.WHITE
		};
		int[][] colors = new int[][] {
			{0, 0, 0}, {0, 0, 170}, {0, 170, 0}, {0, 170, 170}, {170, 0, 0}, {170, 0, 170}, {255, 170, 0}, {170, 170, 170},
			{85, 85, 85}, {85, 85, 255}, {85, 255, 85}, {85, 255, 255}, {255, 85, 85}, {255, 85, 255}, {255, 255, 85}, {255, 255, 255}
		};
		int best = 0;
		int bestDist = Integer.MAX_VALUE;
		for (int i = 0; i < colors.length; i++) {
			int dr = r - colors[i][0];
			int dg = g - colors[i][1];
			int db = b - colors[i][2];
			int dist = dr * dr + dg * dg + db * db;
			if (dist < bestDist) {
				bestDist = dist;
				best = i;
			}
		}
		return palette[best];
	}

	private static int parseHex(String hexColor, int fallback) {
		if (hexColor == null)
			return fallback;
		String h = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;
		if (!h.matches("^[0-9a-fA-F]{6}$"))
			return fallback;
		return Integer.parseInt(h, 16);
	}
}
