package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.minigames.block.entity.AscenscionBlockEntity;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class AscenscionBeamManager {
	private static final Set<BlockPos> BLOCKS = Collections.newSetFromMap(new ConcurrentHashMap<>());

	public static void addBlock(BlockPos pos) {
		if (pos != null) {
			BLOCKS.add(pos.immutable());
		}
	}

	public static void removeBlock(BlockPos pos) {
		if (pos != null) {
			BLOCKS.remove(pos);
		}
	}

	public static void clear() {
		BLOCKS.clear();
	}

	@SubscribeEvent
	public static void onLoggingOut(net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) {
		clear();
	}

	@SubscribeEvent
	public static void onLevelUnload(net.neoforged.neoforge.event.level.LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) {
			clear();
		}
	}

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || BLOCKS.isEmpty()) {
			return;
		}

		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();
		VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lightning());
		boolean renderedAny = false;

		Iterator<BlockPos> it = BLOCKS.iterator();
		while (it.hasNext()) {
			BlockPos pos = it.next();
			if (!mc.level.isLoaded(pos)) {
				continue;
			}
			if (!mc.level.getBlockState(pos).is(MinigamesModBlocks.ASCENSCION.get())) {
				it.remove();
				continue;
			}
			if (pos.distToCenterSqr(cameraPos.x, cameraPos.y, cameraPos.z) > 16384) {
				continue;
			}
			renderAscenscionBeam(mc, pos, buffer, cameraPos);
			renderedAny = true;
		}

		if (renderedAny) {
			bufferSource.endBatch(RenderTypes.lightning());
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || mc.gameRenderer == null || BLOCKS.isEmpty()) {
			return;
		}

		Camera camera = mc.gameRenderer.getMainCamera();
		if (camera == null) {
			return;
		}

		Vec3 cam = camera.position();
		if (isCameraInRay(mc, cam)) {
			int width = event.getGuiGraphics().guiWidth();
			int height = event.getGuiGraphics().guiHeight();
			int yellowTint = (50 << 24) | 0xFFDF33;
			event.getGuiGraphics().fill(0, 0, width, height, yellowTint);
		}
	}

	private static boolean isCameraInRay(Minecraft mc, Vec3 cam) {
		for (BlockPos pos : BLOCKS) {
			if (cam.x >= pos.getX() && cam.x <= pos.getX() + 1.0D &&
				cam.z >= pos.getZ() && cam.z <= pos.getZ() + 1.0D) {
				double y0 = pos.getY() + 1.0D;
				double y1 = AscenscionBlockEntity.getTopY(mc.level, pos);
				if (cam.y >= y0 && cam.y <= y1) {
					return true;
				}
			}
		}
		return false;
	}

	private static void renderAscenscionBeam(Minecraft mc, BlockPos pos, VertexConsumer buffer, Vec3 cameraPos) {
		double y0World = pos.getY() + 1.001D;
		double y1World = AscenscionBlockEntity.getTopY(mc.level, pos);
		if (y1World <= y0World) {
			return;
		}

		float x0 = (float) (pos.getX() - cameraPos.x);
		float y0 = (float) (y0World - cameraPos.y);
		float y1 = (float) (y1World - cameraPos.y);
		float z0 = (float) (pos.getZ() - cameraPos.z);

		float r = 1.0F;
		float g = 0.78F;
		float b = 0.20F;
		float alpha = 0.75F;

		float xMin = x0 + (isAscenscion(mc, pos.west()) ? 0.0F : 0.001F);
		float xMax = x0 + (isAscenscion(mc, pos.east()) ? 1.0F : 0.999F);
		float zMin = z0 + (isAscenscion(mc, pos.north()) ? 0.0F : 0.001F);
		float zMax = z0 + (isAscenscion(mc, pos.south()) ? 1.0F : 0.999F);

		if (!isAscenscion(mc, pos.north())) {
			addQuad(buffer, xMin, y0, zMin, xMin, y1, zMin, xMax, y1, zMin, xMax, y0, zMin, r, g, b, alpha);
		}
		if (!isAscenscion(mc, pos.south())) {
			addQuad(buffer, xMax, y0, zMax, xMax, y1, zMax, xMin, y1, zMax, xMin, y0, zMax, r, g, b, alpha);
		}
		if (!isAscenscion(mc, pos.west())) {
			addQuad(buffer, xMin, y0, zMax, xMin, y1, zMax, xMin, y1, zMin, xMin, y0, zMin, r, g, b, alpha);
		}
		if (!isAscenscion(mc, pos.east())) {
			addQuad(buffer, xMax, y0, zMin, xMax, y1, zMin, xMax, y1, zMax, xMax, y0, zMax, r, g, b, alpha);
		}

		addQuad(buffer, xMin, y0, zMin, xMin, y0, zMax, xMax, y0, zMax, xMax, y0, zMin, r, g, b, alpha);
		addQuad(buffer, xMin, y1, zMin, xMax, y1, zMin, xMax, y1, zMax, xMin, y1, zMax, r, g, b, alpha);
	}

	private static boolean isAscenscion(Minecraft mc, BlockPos pos) {
		return mc.level != null && mc.level.getBlockState(pos).is(MinigamesModBlocks.ASCENSCION.get());
	}

	private static void addVertex(VertexConsumer buffer, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		buffer.addVertex(x, y, z)
				.setColor(r, g, b, a)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(15728880)
				.setNormal(0.0F, 1.0F, 0.0F);
	}

	private static void addQuad(VertexConsumer buffer,
								float x1, float y1, float z1,
								float x2, float y2, float z2,
								float x3, float y3, float z3,
								float x4, float y4, float z4,
								float r, float g, float b, float a) {
		addVertex(buffer, x1, y1, z1, 0.0F, 1.0F, r, g, b, a);
		addVertex(buffer, x2, y2, z2, 0.0F, 0.0F, r, g, b, a);
		addVertex(buffer, x3, y3, z3, 1.0F, 0.0F, r, g, b, a);
		addVertex(buffer, x4, y4, z4, 1.0F, 1.0F, r, g, b, a);
	}
}
