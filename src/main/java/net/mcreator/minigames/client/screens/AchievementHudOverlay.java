package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.*;

@EventBusSubscriber(Dist.CLIENT)
public class AchievementHudOverlay {
	private static final Identifier SPRITE_0 = Identifier.parse("minigames:textures/screens/hotbarslot.png");
	private static final Identifier SPRITE_1 = Identifier.parse("minigames:textures/screens/hud.png");
	private static final Identifier SPRITE_2 = Identifier.parse("minigames:textures/screens/modifiers.png");
	private static final Identifier SPRITE_3 = Identifier.parse("minigames:textures/screens/pvpanimation.png");
	private static final Identifier SPRITE_4 = Identifier.parse("minigames:textures/screens/compass.png");

	@SubscribeEvent(priority = EventPriority.HIGHEST)
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
		if (ShowDefaultTimerProcedure.execute(world, entity)) {
			if (ShowCompassHunterProcedure.execute(world, entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, w / 2 + 90, h - 23, 0, 0, 29, 24, 29, 24);
			}
			if (HidePVPIconHuntProcedure.execute(world)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, 2, h - 45, 0, Mth.clamp((int) PVPDisplayProcedure.execute(world) * 50, 0, 100), 50, 50, 50, 150);
			}

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, 42, h - 47, 0, Mth.clamp((int) DisplayThunderProcedure.execute(world) * 50, 0, 200), 50, 50, 50, 250);

			if (DisplayPVPProcedure.execute(world)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_3, w / 2 + -274, h / 2 + -89, 0, Mth.clamp((int) AnimatePVPProcedure.execute(world) * 67, 0, 603), 600, 67, 600, 670);
			}
			if (ShowCompassHunterProcedure.execute(world, entity)) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_4, w / 2 + 100, h - 19, 0, Mth.clamp((int) CompassHunterSpriteProcedure.execute(world, entity) * 16, 0, 512), 16, 16, 16, 528);
			}
		}
	}
}