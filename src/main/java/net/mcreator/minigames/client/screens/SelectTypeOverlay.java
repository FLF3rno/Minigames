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

import net.mcreator.minigames.procedures.SpriteRevealDisplayProcedure;
import net.mcreator.minigames.procedures.SpriteAchievementTypeDisplayProcedure;
import net.mcreator.minigames.procedures.DisplayTypeAchievmentProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class SelectTypeOverlay {
	private static final Identifier SPRITE_0 = Identifier.parse("minigames:textures/screens/windowanimation.png");
	private static final Identifier SPRITE_1 = Identifier.parse("minigames:textures/screens/windowbreak.png");

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
		if (DisplayTypeAchievmentProcedure.execute(world)) {

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, w / 2 + -189, h / 2 + -112, 0, Mth.clamp((int) SpriteAchievementTypeDisplayProcedure.execute(world) * 384, 0, 1152), 384, 384, 384, 1536);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, w / 2 + -189, h / 2 + -112, 0, Mth.clamp((int) SpriteRevealDisplayProcedure.execute(world) * 384, 0, 3840), 384, 384, 384, 4224);

		}
	}
}