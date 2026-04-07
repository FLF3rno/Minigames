package net.mcreator.minigames.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.ReminderAchievementIconProcedure;
import net.mcreator.minigames.procedures.DisplayReminderProcedure;
import net.mcreator.minigames.procedures.AchievementTitleProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class AchievementReminderOverlay {
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("minigames:textures/screens/achievementpopup.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("minigames:textures/screens/achievementicons.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(ScreenEvent.Render.Post event) {
		if (event.getScreen() instanceof InventoryScreen) {
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
			if (DisplayReminderProcedure.execute(world)) {

				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, w / 2 + -80, h / 2 + -118, 0, 0, 160, 32, 160, 32);

				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, w / 2 + -73, h / 2 + -110, 0, Mth.clamp((int) ReminderAchievementIconProcedure.execute(world) * 16, 0, 1216), 16, 16, 16, 1232);

				event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.minigames.achievement_reminder.label_obtain_this_achievement"), w / 2 + -52, h / 2 + -112, -1214228, false);
				event.getGuiGraphics().drawString(Minecraft.getInstance().font,

						AchievementTitleProcedure.execute(world), w / 2 + -52, h / 2 + -101, -1, false);
			}
		}
	}
}