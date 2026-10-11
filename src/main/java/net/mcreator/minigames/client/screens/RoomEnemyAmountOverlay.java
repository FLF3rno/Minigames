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

import net.mcreator.minigames.procedures.RoomEnemyAmountDisplayProcedure;
import net.mcreator.minigames.procedures.EnemyAmountReturnProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class RoomEnemyAmountOverlay {
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/fightroom.png");

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
		if (RoomEnemyAmountDisplayProcedure.execute(world)) {
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, w / 2 + -27, 22, 0, 0, 16, 16, 16, 16);

			event.getGuiGraphics().text(Minecraft.getInstance().font,

					EnemyAmountReturnProcedure.execute(world), w / 2 + -8, 26, -1336, true);
		}
	}
}