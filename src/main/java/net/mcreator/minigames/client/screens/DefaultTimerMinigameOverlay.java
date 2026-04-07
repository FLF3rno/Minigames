package net.mcreator.minigames.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.*;

@EventBusSubscriber(Dist.CLIENT)
public class DefaultTimerMinigameOverlay {
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_2 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_3 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_4 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_5 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_6 = ResourceLocation.parse("minigames:textures/screens/timer2.png");
	private static final ResourceLocation SPRITE_7 = ResourceLocation.parse("minigames:textures/screens/timer2.png");

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

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, w / 2 + -69, 2, 0, Mth.clamp((int) DisplayHoursProcedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, w / 2 + -51, 2, 0, Mth.clamp((int) DisplayHours2Procedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, w / 2 + -37, 2, 0, 250, 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_3, w / 2 + -23, 2, 0, Mth.clamp((int) DisplayMinutesProcedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_4, w / 2 + -5, 2, 0, Mth.clamp((int) DisplayMinutes2Procedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_5, w / 2 + 9, 2, 0, 250, 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_6, w / 2 + 23, 2, 0, Mth.clamp((int) DisplaySecondsProcedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_7, w / 2 + 41, 2, 0, Mth.clamp((int) DisplaySeconds2Procedure.execute(world) * 25, 0, 250), 25, 25, 25, 275);

		}
	}
}