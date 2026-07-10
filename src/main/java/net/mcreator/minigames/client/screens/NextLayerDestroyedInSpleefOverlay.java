package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.NextLayerDestroyedInDisplayProcedure;
import net.mcreator.minigames.procedures.DisplaySpleefTimerProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class NextLayerDestroyedInSpleefOverlay {
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
		if (DisplaySpleefTimerProcedure.execute(world)) {
			event.getGuiGraphics().text(Minecraft.getInstance().font,

					NextLayerDestroyedInDisplayProcedure.execute(world), w / 2 + -66, 4, -16777216, false);
			event.getGuiGraphics().text(Minecraft.getInstance().font,

					NextLayerDestroyedInDisplayProcedure.execute(world), w / 2 + -65, 3, -16777216, false);
			event.getGuiGraphics().text(Minecraft.getInstance().font,

					NextLayerDestroyedInDisplayProcedure.execute(world), w / 2 + -65, 5, -16777216, false);
			event.getGuiGraphics().text(Minecraft.getInstance().font,

					NextLayerDestroyedInDisplayProcedure.execute(world), w / 2 + -64, 4, -16777216, false);
			event.getGuiGraphics().text(Minecraft.getInstance().font,

					NextLayerDestroyedInDisplayProcedure.execute(world), w / 2 + -65, 4, -1, false);
		}
	}
}