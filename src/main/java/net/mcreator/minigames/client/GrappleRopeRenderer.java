package net.mcreator.minigames.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.mcreator.minigames.entity.GrappleEntity;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class GrappleRopeRenderer {
	private static final double SEARCH_RADIUS = 96.0D;
	private static final float ROPE_HALF_WIDTH = 0.03F;
	private static final float ROPE_ALPHA = 0.85F;
	private static final float ROPE_DEPTH_BIAS = 0.0025F;

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent.AfterEntities event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null)
			return;

		Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer ropeBuffer = buffers.getBuffer(RenderType.debugQuads());

		List<GrappleEntity> grapples = mc.level.getEntitiesOfClass(GrappleEntity.class, new AABB(cameraPos, cameraPos).inflate(SEARCH_RADIUS), Entity::isAlive);
		for (GrappleEntity grapple : grapples) {
			Entity owner = grapple.getOwner();
			if (!(owner instanceof Player player) || !player.isAlive())
				continue;

			Vec3 start = player.getEyePosition(event.getPartialTick().getGameTimeDeltaPartialTick(false)).subtract(cameraPos);
			Vec3 end = grapple.getPosition(event.getPartialTick().getGameTimeDeltaPartialTick(false)).subtract(cameraPos);
			drawQuadBeam(ropeBuffer, start, end, cameraPos, 0.95F, 0.95F, 0.95F, ROPE_ALPHA, ROPE_HALF_WIDTH);
		}

		buffers.endBatch(RenderType.debugQuads());
	}

	private static void drawQuadBeam(VertexConsumer consumer, Vec3 start, Vec3 end, Vec3 cameraWorldPos, float r, float g, float b, float a, float halfWidth) {
		Vec3 dir = end.subtract(start);
		if (dir.lengthSqr() < 1.0E-6D)
			return;
		dir = dir.normalize();

		Vec3 midWorld = start.add(end).scale(0.5D).add(cameraWorldPos);
		Vec3 toCamera = cameraWorldPos.subtract(midWorld);
		if (toCamera.lengthSqr() < 1.0E-6D)
			toCamera = new Vec3(0.0D, 1.0D, 0.0D);
		toCamera = toCamera.normalize();

		Vec3 right = dir.cross(toCamera);
		if (right.lengthSqr() < 1.0E-6D)
			right = dir.cross(new Vec3(0.0D, 1.0D, 0.0D));
		if (right.lengthSqr() < 1.0E-6D)
			right = new Vec3(1.0D, 0.0D, 0.0D);
		right = right.normalize().scale(halfWidth);

		Vec3 bias = toCamera.scale(ROPE_DEPTH_BIAS);
		Vec3 s1 = start.add(right).add(bias);
		Vec3 s2 = start.subtract(right).add(bias);
		Vec3 e1 = end.add(right).add(bias);
		Vec3 e2 = end.subtract(right).add(bias);

		float nx = Mth.clamp((float) dir.x, -1.0F, 1.0F);
		float ny = Mth.clamp((float) dir.y, -1.0F, 1.0F);
		float nz = Mth.clamp((float) dir.z, -1.0F, 1.0F);

		consumer.addVertex((float) s1.x, (float) s1.y, (float) s1.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
		consumer.addVertex((float) e1.x, (float) e1.y, (float) e1.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
		consumer.addVertex((float) e2.x, (float) e2.y, (float) e2.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
		consumer.addVertex((float) s2.x, (float) s2.y, (float) s2.z).setColor(r, g, b, a).setNormal(nx, ny, nz);
	}
}
