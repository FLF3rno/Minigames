package net.mcreator.minigames.client;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minigames.FreeBeamRenderManager;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = MinigamesMod.MODID, value = Dist.CLIENT)
public final class AscendingWorldGrayscaleRender {
	private static final ResourceLocation GRAYSCALE_POST_CHAIN_ID = ResourceLocation.fromNamespaceAndPath("minigames", "ascending_grayscale");
	private static final ResourceLocation OVERSAT_POST_CHAIN_ID = ResourceLocation.fromNamespaceAndPath("minigames", "ascending_oversaturate");
	private static final CrossFrameResourcePool RESOURCE_POOL = new CrossFrameResourcePool(3);
	private static boolean warnedShaderFailure = false;

	private AscendingWorldGrayscaleRender() {
	}

	@SubscribeEvent
	public static void onAfterLevel(RenderLevelStageEvent.AfterLevel event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null) {
			return;
		}

		// If you are the one ascending, apply a "beautiful/oversaturated" filter and skip grayscale logic.
		if (mc.player.hasEffect(MinigamesModMobEffects.ASCENDING)) {
			try {
				PostChain postChain = mc.getShaderManager().getPostChain(OVERSAT_POST_CHAIN_ID, net.minecraft.client.renderer.LevelTargetBundle.MAIN_TARGETS);
				if (postChain == null) {
					return;
				}
				RenderSystem.resetTextureMatrix();
				postChain.process(mc.getMainRenderTarget(), RESOURCE_POOL);
			} catch (Throwable ignored) {
				// If shaders fail to compile/load for any reason, avoid hard-crashing the client.
			}
			return;
		}

		boolean anyAscending = false;
		for (Player player : mc.level.players()) {
			if (player.hasEffect(MinigamesModMobEffects.ASCENDING)) {
				anyAscending = true;
				break;
			}
		}
		if (!anyAscending) {
			return;
		}

		try {
			PostChain postChain = mc.getShaderManager().getPostChain(GRAYSCALE_POST_CHAIN_ID, net.minecraft.client.renderer.LevelTargetBundle.MAIN_TARGETS);
			if (postChain == null) {
				return;
			}

			// Convert the already-rendered scene to grayscale.
			RenderSystem.resetTextureMatrix();
			postChain.process(mc.getMainRenderTarget(), RESOURCE_POOL);
		} catch (Throwable ignored) {
			// If shaders fail to compile/load for any reason, avoid hard-crashing the client.
			if (!warnedShaderFailure) {
				warnedShaderFailure = true;
				MinigamesMod.LOGGER.warn("Ascending grayscale post effect failed to load/process; skipping effect.", ignored);
			}
			return;
		}

		// In third-person, re-rendering the player/beams on top of the grayscaled scene tends to look like a
		// slightly offset duplicate (because you're seeing the original grayscale render + the color overlay).
		// Keep third-person clean: grayscale only.
		CameraType cameraType = mc.options.getCameraType();
		if (cameraType != null && !cameraType.isFirstPerson()) {
			return;
		}

		// Re-render ascending players and their beams in full color on top.
		Vec3 cameraPos = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

		FreeBeamRenderManager.renderBeams(poseStack, bufferSource, cameraPos, mc.level.players(), event.getPartialTick().getGameTimeDeltaPartialTick(false));

		EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		for (Player player : mc.level.players()) {
			if (!player.hasEffect(MinigamesModMobEffects.ASCENDING)) {
				continue;
			}

			double x = player.getX() - cameraPos.x;
			double y = player.getY() - cameraPos.y;
			double z = player.getZ() - cameraPos.z;
			int packedLight = dispatcher.getPackedLightCoords(player, partialTick);
			dispatcher.render(player, x, y, z, player.getYRot(), poseStack, bufferSource, packedLight);
		}

		// Avoid flushing every batched RenderType here (can break later passes / F5).
		// Flush only the types we used in this late overlay pass.
		bufferSource.endBatch(RenderType.beaconBeam(FreeBeamRenderManager.WHITE_TEXTURE, true));
	}
}
