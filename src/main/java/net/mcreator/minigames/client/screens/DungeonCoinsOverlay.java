package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.ShowDungeonCoinsProcedure;
import net.mcreator.minigames.procedures.DungeonCoinsDisplayProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class DungeonCoinsOverlay {
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/dungeoncoins.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
		}
		if (ShowDungeonCoinsProcedure.execute(world)) {
			// Render icon 2x bigger (32x32)
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, 5, h - 37, 0, 0, 32, 32, 32, 32);

			// Render text 2x bigger
			event.getGuiGraphics().pose().pushMatrix();
			event.getGuiGraphics().pose().translate(40, h - 30);
			event.getGuiGraphics().pose().scale(2.0F, 2.0F);
			event.getGuiGraphics().text(Minecraft.getInstance().font, DungeonCoinsDisplayProcedure.execute(world), 0, 0, -409536, true);
			event.getGuiGraphics().pose().popMatrix();
		}
	}
}

