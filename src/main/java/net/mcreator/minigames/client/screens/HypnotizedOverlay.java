package net.mcreator.minigames.client.screens;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.init.MinigamesModMobEffects;

@EventBusSubscriber(Dist.CLIENT)
public class HypnotizedOverlay {
	private static final ResourceLocation RINGS = ResourceLocation.parse("minigames:textures/screens/hypnosis_rings_overlay.png");
	private static final float TEXTURE_WIDTH = 958f;
	private static final float TEXTURE_HEIGHT = 538f;
	private static final float LAYER_DURATION = 52f;
	private static final float OVERSCAN = 1.28f;
	private static final float SCALE_MIN = 1.02f;
	private static final float SCALE_MAX = 2.35f;
	private static final float[] PHASES = new float[] { 0f, 1f / 3f, 2f / 3f };
	private static final float[] ROTATIONS = new float[] { 7f, -5f, 6f };

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null || !player.hasEffect(MinigamesModMobEffects.HYPNOTIZED)) {
			return;
		}

		int width = event.getGuiGraphics().guiWidth();
		int height = event.getGuiGraphics().guiHeight();
		float effectTime = player.tickCount + event.getPartialTick().getGameTimeDeltaPartialTick(false);
		float fadeIn = Mth.clamp((player.getEffect(MinigamesModMobEffects.HYPNOTIZED) != null ? player.getEffect(MinigamesModMobEffects.HYPNOTIZED).getDuration() : 0) / 20f, 0f, 1f);

		event.getGuiGraphics().fill(0, 0, width, height, ((int) (fadeIn * 18) << 24));
		for (int i = 0; i < PHASES.length; i++) {
			drawLayer(event, width, height, effectTime, PHASES[i], ROTATIONS[i], fadeIn);
		}
	}

	private static void drawLayer(RenderGuiEvent.Post event, int screenWidth, int screenHeight, float effectTime, float phaseOffset, float rotationDirection, float fadeIn) {
		float progress = ((effectTime / LAYER_DURATION) + phaseOffset) % 1f;
		float layerScale = Mth.lerp(progress, SCALE_MIN, SCALE_MAX);
		float visibility = fadeIn * smoothPulse(progress);
		if (visibility <= 0.01f) {
			return;
		}
		float rotation = (effectTime * 0.65f + phaseOffset * 180f) * rotationDirection;

		float coverScale = Math.max(screenWidth / TEXTURE_WIDTH, screenHeight / TEXTURE_HEIGHT) * OVERSCAN;
		int drawWidth = Math.round(TEXTURE_WIDTH * coverScale);
		int drawHeight = Math.round(TEXTURE_HEIGHT * coverScale);
		int tint = ((int) (visibility * 255f) << 24) | 0xFFFFFF;

		event.getGuiGraphics().pose().pushMatrix();
		event.getGuiGraphics().pose().translate(screenWidth / 2f, screenHeight / 2f);
		event.getGuiGraphics().pose().rotate((float) Math.toRadians(rotation));
		event.getGuiGraphics().pose().scale(layerScale, layerScale);
		event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, RINGS, -drawWidth / 2, -drawHeight / 2, 0, 0, drawWidth, drawHeight, drawWidth, drawHeight, tint);
		event.getGuiGraphics().pose().popMatrix();
	}

	private static float smoothPulse(float progress) {
		float edgeFade = 0.22f;
		if (progress < edgeFade) {
			return (float) Mth.smoothstep(progress / edgeFade);
		}
		if (progress > 1f - edgeFade) {
			return (float) Mth.smoothstep((1f - progress) / edgeFade);
		}
		return 1f;
	}
}
