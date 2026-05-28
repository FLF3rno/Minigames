package net.mcreator.minigames.client;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.ModDataAttachments;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.lang.reflect.Method;

@EventBusSubscriber(modid = MinigamesMod.MODID, value = Dist.CLIENT)
public final class AscendingWorldGrayscaleRender {
	private static final ResourceLocation GRAYSCALE_CHAIN_ID = ResourceLocation.fromNamespaceAndPath("minigames", "ascending_grayscale");
	private static boolean warnedShaderFailure = false;

	private AscendingWorldGrayscaleRender() {
	}

	@SubscribeEvent
	public static void onAfterLevel(RenderLevelStageEvent.AfterLevel event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.getShaderManager() == null) {
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
			PostChain chain = loadChain(mc);
			if (chain == null) {
				return;
			}
			chain.process(mc.getMainRenderTarget(), GraphicsResourceAllocator.UNPOOLED);
			renderBeamPlayersInColor(mc);
		} catch (Throwable ignored) {
			if (!warnedShaderFailure) {
				warnedShaderFailure = true;
				MinigamesMod.LOGGER.warn("Ascending grayscale post effect failed to load/process; skipping effect.", ignored);
			}
		}
	}

	private static void renderBeamPlayersInColor(Minecraft mc) {
		if (mc.level == null || mc.gameRenderer == null) {
			return;
		}

		Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
		float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		PoseStack poseStack = new PoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

		for (Player player : mc.level.players()) {
			if (!player.isAlive() || !player.hasData(ModDataAttachments.BEAM_DATA)) {
				continue;
			}

			ModDataAttachments.BeamData data = player.getData(ModDataAttachments.BEAM_DATA);
			if (!data.hasBeam) {
				continue;
			}

			poseStack.pushPose();
			double x = player.getX() - cameraPos.x;
			double y = player.getY() - cameraPos.y;
			double z = player.getZ() - cameraPos.z;
			mc.getEntityRenderDispatcher().render(player, x, y, z, player.getYRot(), poseStack, bufferSource, 15728880);
			poseStack.popPose();
		}

		bufferSource.endBatch();
	}

	private static PostChain loadChain(Minecraft mc) throws Exception {
		Object shaderManager = mc.getShaderManager();
		for (Method method : shaderManager.getClass().getMethods()) {
			if (!PostChain.class.isAssignableFrom(method.getReturnType())) {
				continue;
			}
			Class<?>[] params = method.getParameterTypes();
			if (params.length == 2 && ResourceLocation.class.isAssignableFrom(params[0]) && java.util.Set.class.isAssignableFrom(params[1])) {
				Object result = method.invoke(shaderManager, GRAYSCALE_CHAIN_ID, net.minecraft.client.renderer.LevelTargetBundle.MAIN_TARGETS);
				return (PostChain) result;
			}
		}
		return null;
	}
}
