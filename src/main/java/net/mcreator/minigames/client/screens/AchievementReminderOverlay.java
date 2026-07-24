package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class AchievementReminderOverlay {
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/achievementpopup.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(ScreenEvent.Render.Post event) {
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
				int popupWidth = 160;
				int popupHeight = 32;

				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, popupX, popupY, 0, 0, popupWidth, popupHeight, 160, 32);

				int iconX = (w / 2) - 72;
				int iconY = (h / 2) - 110;
				var achievementItem = MinigamesModVariables.MapVariables.get(world).AchievementIcon;
				event.getGuiGraphics().item(achievementItem, iconX, iconY);

				event.getGuiGraphics().text(Minecraft.getInstance().font, Component.translatable("gui.minigames.achievement_reminder.label_obtain_this_achievement"), w / 2 - 52, h / 2 - 112, -1214228, false);
				event.getGuiGraphics().text(Minecraft.getInstance().font, Component.literal(MinigamesModVariables.MapVariables.get(world).AchievementTitle), w / 2 - 52, h / 2 - 101, -1, false);

				if (mouseX >= popupX && mouseX <= popupX + popupWidth && mouseY >= popupY && mouseY <= popupY + popupHeight) {


					event.getGuiGraphics().setTooltipForNextFrame(
							Minecraft.getInstance().font,
							Component.translatable(MinigamesModVariables.MapVariables.get(world).AchievementDescription),
							mouseX,
							mouseY
					);
				}
			}
		}
	}
}