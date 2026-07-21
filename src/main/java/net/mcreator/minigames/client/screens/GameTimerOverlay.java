package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(Dist.CLIENT)
public class GameTimerOverlay {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		if (MinigamesModVariables.MapVariables.get(world).ShowTimer) {
			int hh = (int) Math.round(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerHours);
			int mm = (int) Math.round(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes);
			int ss = (int) Math.round(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds);
			float scale = (float) entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerScale;
			String timer;
			if (hh > 0) {
				timer = String.format("%02d:%02d:%02d", hh, mm, ss);
			} else {
				timer = String.format("%02d:%02d", mm, ss);
			}
			Component text = Component.literal(timer).withStyle(ChatFormatting.BOLD);

			int textWidth = Minecraft.getInstance().font.width(text);

			int xpos = Math.round(((w / 2F) / scale) - (textWidth / 2F));
			int ypos = (int) (5 / scale);
			String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).TimerColor
					.trim()
					.replace("#", "")
					.replace("0x", "");
			int rgb = 0xFFFFFF;
			try {
				rgb = Integer.parseUnsignedInt("FF" + color, 16);
			} catch (NumberFormatException ignored) {
			}
			event.getGuiGraphics().pose().pushMatrix();
			event.getGuiGraphics().pose().scale(scale, scale);
			event.getGuiGraphics().text(Minecraft.getInstance().font, text, xpos, ypos, rgb, true);
			event.getGuiGraphics().pose().popMatrix();
		}
	}
}