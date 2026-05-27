package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.ModDataAttachments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class BeamScreenTintOverlay {
	private static final float BEAM_HEIGHT = 20.0f;
	private static final float BEAM_BOTTOM_RADIUS = 1.5f;
	private static final float BEAM_TOP_RADIUS = 0.25f;

	// Matches FreeBeamRenderManager beam color (1.0, 0.88, 0.35)
	private static final int BEAM_COLOR_RGB = 0xFFE059;
	private static final int MAX_ALPHA = 120;

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || mc.gameRenderer == null) {
			return;
		}

		Camera camera = mc.gameRenderer.getMainCamera();
		Vec3 point = camera != null ? camera.getPosition() : mc.player.getEyePosition();
		float intensity = getBeamIntensity(point, mc.level.players());
		if (intensity <= 0f) {
			return;
		}

		int width = event.getGuiGraphics().guiWidth();
		int height = event.getGuiGraphics().guiHeight();
		int alpha = Mth.clamp((int) (MAX_ALPHA * intensity), 0, 255);
		event.getGuiGraphics().fill(0, 0, width, height, (alpha << 24) | BEAM_COLOR_RGB);
	}

	private static float getBeamIntensity(Vec3 point, Iterable<? extends Player> players) {
		float strongest = 0f;
		for (Player p : players) {
			if (!p.hasData(ModDataAttachments.BEAM_DATA)) {
				continue;
			}
			ModDataAttachments.BeamData data = p.getData(ModDataAttachments.BEAM_DATA);
			if (!data.hasBeam) {
				continue;
			}
			strongest = Math.max(strongest, computeInsideStrength(point, data));
			if (strongest >= 1f) {
				return 1f;
			}
		}
		return strongest;
	}

	private static float computeInsideStrength(Vec3 point, ModDataAttachments.BeamData data) {
		double relY = point.y - data.y;
		if (relY < 0 || relY > BEAM_HEIGHT) {
			return 0f;
		}

		double t = relY / BEAM_HEIGHT;
		double radius = Mth.lerp((float) t, BEAM_BOTTOM_RADIUS, BEAM_TOP_RADIUS);
		double dx = point.x - data.x;
		double dz = point.z - data.z;
		double dist = Math.sqrt(dx * dx + dz * dz);
		if (dist > radius) {
			return 0f;
		}

		double proximity = 1.0 - (dist / Math.max(radius, 0.001));
		double minStrength = 0.45;
		return (float) Mth.clamp(minStrength + proximity * (1.0 - minStrength), 0.0, 1.0);
	}
}
