package net.mcreator.minigames.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.SnowballCounterTextProcedure;
import net.mcreator.minigames.procedures.DisplaySnowballCounterProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class SnowballCounterSpleefOverlay {
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/snowball.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
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
		if (DisplaySnowballCounterProcedure.execute(world, entity)) {
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, w / 2 + 94, h - 19, 0, 0, 16, 16, 16, 16);

			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					SnowballCounterTextProcedure.execute(entity), w / 2 + 111, h - 15, -16777216, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					SnowballCounterTextProcedure.execute(entity), w / 2 + 110, h - 14, -16777216, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					SnowballCounterTextProcedure.execute(entity), w / 2 + 111, h - 13, -16777216, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					SnowballCounterTextProcedure.execute(entity), w / 2 + 112, h - 14, -16777216, false);
			event.getGuiGraphics().drawString(Minecraft.getInstance().font,

					SnowballCounterTextProcedure.execute(entity), w / 2 + 111, h - 14, -1, false);
		}
	}
}