package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.PowerupChecklistProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class AchievementReminderOverlay {
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/achievementpopup.png");


	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(ScreenEvent.Render.Post event) {
		if (event.getScreen() instanceof InventoryScreen) {
			int w = event.getGuiGraphics().guiWidth();
			int h = event.getGuiGraphics().guiHeight();
			Level world = null;
			double x = 0;
			double y = 0;
			Player entity = Minecraft.getInstance().player;
			if (entity != null) {
				world = entity.level();
			}
			if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {

				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, w / 2 + -80, h / 2 + -118, 0, 0, 160, 32, 160, 32);

				x = Math.round(w / 2) - 72;
				y = Math.round(h / 2) + -110;
				event.getGuiGraphics().item(MinigamesModVariables.MapVariables.get(world).AchievementIcon, (int) x, (int) y);

				event.getGuiGraphics().text(Minecraft.getInstance().font, Component.translatable("gui.minigames.achievement_reminder.label_obtain_this_achievement"), w / 2 + -52, h / 2 + -112, -1214228, false);
				event.getGuiGraphics().text(Minecraft.getInstance().font, Component.literal(MinigamesModVariables.MapVariables.get(world).AchievementTitle), w / 2 + -52, h / 2 + -101, -1, false);
			}
		}
	}
}