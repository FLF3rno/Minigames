package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

@EventBusSubscriber(value = Dist.CLIENT)
public class AchievementReminderOverlay {
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/achievementpopup.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onScreenRender(ScreenEvent.Render.Post event) {
		if (event.getScreen() instanceof InventoryScreen) {
			int w = event.getGuiGraphics().guiWidth();
			int h = event.getGuiGraphics().guiHeight();

			int mouseX = event.getMouseX();
			int mouseY = event.getMouseY();

			Player entity = Minecraft.getInstance().player;
			if (entity == null) return;
			Level world = entity.level();

			if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
				int popupX = w / 2 - 80;
				int popupY = h / 2 - 118;
				renderReminder(event.getGuiGraphics(), world, popupX, popupY, mouseX, mouseY, true);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onGuiRender(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player entity = mc.player;
		if (entity == null || mc.level == null) return;
		if (mc.screen instanceof InventoryScreen) return;

		boolean alwaysShow = MinigamesSettingsConfig.getBoolean("AlwaysShowReminder",
				entity.getData(MinigamesModVariables.PLAYER_VARIABLES).AlwaysShowReminder);
		if (!alwaysShow) return;

		Level world = mc.level;
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			int popupX = 5;
			int popupY = 5;
			renderReminder(event.getGuiGraphics(), world, popupX, popupY, -1, -1, false);
		}
	}

	private static void renderReminder(GuiGraphicsExtractor graphics, Level world, int popupX, int popupY, int mouseX, int mouseY, boolean checkHover) {
		int popupWidth = 160;
		int popupHeight = 32;

		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, popupX, popupY, 0, 0, popupWidth, popupHeight, 160, 32);

		int iconX = popupX + 8;
		int iconY = popupY + 8;
		var achievementItem = MinigamesModVariables.MapVariables.get(world).AchievementIcon;
		graphics.item(achievementItem, iconX, iconY);

		graphics.text(Minecraft.getInstance().font, Component.translatable("gui.minigames.achievement_reminder.label_obtain_this_achievement"), popupX + 28, popupY + 6, -1214228, false);
		graphics.text(Minecraft.getInstance().font, Component.literal(MinigamesModVariables.MapVariables.get(world).AchievementTitle), popupX + 28, popupY + 17, -1, false);

		if (checkHover && mouseX >= popupX && mouseX <= popupX + popupWidth && mouseY >= popupY && mouseY <= popupY + popupHeight) {
			graphics.setTooltipForNextFrame(
					Minecraft.getInstance().font,
					Component.translatable(MinigamesModVariables.MapVariables.get(world).AchievementDescription),
					mouseX,
					mouseY
			);
		}
	}
}