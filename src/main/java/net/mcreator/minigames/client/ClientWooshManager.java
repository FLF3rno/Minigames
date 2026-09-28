package net.mcreator.minigames.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class ClientWooshManager {

	private static final List<WooshInstance> WOOSHES = new CopyOnWriteArrayList<>();

	public static class WooshInstance {
		double x, y, z;
		final int entityId;
		final float size;
		final float speed;
		final int totalTicks;
		int remainingTicks;
		final int color;

		WooshInstance(double x, double y, double z, int entityId, float size, float speed, int ticks, int color) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.entityId = entityId;
			this.size = Math.max(0.5f, size);
			this.speed = Math.max(0.1f, speed);
			this.totalTicks = Math.max(1, ticks);
			this.remainingTicks = this.totalTicks;
			this.color = color;
		}
	}

	public static void addWoosh(double x, double y, double z, int entityId, float size, float speed, int ticks, int color) {
		WOOSHES.add(new WooshInstance(x, y, z, entityId, size, speed, ticks, color));
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (WOOSHES.isEmpty()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			WOOSHES.clear();
			return;
		}

		for (WooshInstance woosh : WOOSHES) {
			if (woosh.entityId >= 0) {
				Entity ent = mc.level.getEntity(woosh.entityId);
				if (ent != null && ent.isAlive()) {
					woosh.x = ent.getX();
					woosh.y = ent.getY();
					woosh.z = ent.getZ();
				}
			}

			woosh.remainingTicks--;
			if (woosh.remainingTicks <= 0) {
				WOOSHES.remove(woosh);
			}
		}
	}

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
		if (WOOSHES.isEmpty()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) {
			return;
		}

		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.debugQuads());
		Vec3 camera = mc.gameRenderer.getMainCamera().position();
		float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		Matrix4f matrix = poseStack.last().pose();

		for (WooshInstance woosh : WOOSHES) {
			double cx = woosh.x;
			double cy = woosh.y;
			double cz = woosh.z;

			if (woosh.entityId >= 0) {
				Entity ent = mc.level.getEntity(woosh.entityId);
				if (ent != null) {
					cx = Mth.lerp(partialTick, ent.xo, ent.getX());
					cy = Mth.lerp(partialTick, ent.yo, ent.getY());
					cz = Mth.lerp(partialTick, ent.zo, ent.getZ());
				}
			}

			float relX = (float) (cx - camera.x);
			float relY = (float) (cy - camera.y);
			float relZ = (float) (cz - camera.z);

			float elapsed = (woosh.totalTicks - woosh.remainingTicks) + partialTick;
			float progress = Mth.clamp(elapsed / woosh.totalTicks, 0.0f, 1.0f);

			float easeOut = 1.0f - (float) Math.pow(1.0f - progress, 2.5);
			float alphaFade = 1.0f - (progress * progress);

			int r = (woosh.color >> 16) & 0xFF;
			int g = (woosh.color >> 8) & 0xFF;
			int b = woosh.color & 0xFF;

			float r1 = woosh.size * (0.28f + 0.72f * easeOut);
			float thick1 = woosh.size * 0.22f * (1.0f - 0.45f * progress);
			float rot1 = elapsed * woosh.speed * 0.48f;
			float y1 = relY + 0.12f;
			float alpha1 = alphaFade * 0.85f;

			drawWindArc(consumer, matrix, relX, y1, relZ, r1, thick1, rot1, 1.48f, r, g, b, alpha1, 14);
			drawWindArc(consumer, matrix, relX, y1, relZ, r1, thick1, rot1 + 2.094f, 1.48f, r, g, b, alpha1, 14);
			drawWindArc(consumer, matrix, relX, y1, relZ, r1, thick1, rot1 + 4.188f, 1.48f, r, g, b, alpha1, 14);

			float r2 = woosh.size * 0.72f * (0.22f + 0.78f * easeOut);
			float thick2 = woosh.size * 0.17f * (1.0f - 0.5f * progress);
			float rot2 = elapsed * woosh.speed * 0.72f + 1.047f;
			float y2 = relY + 0.45f + 0.35f * progress;
			float alpha2 = alphaFade * 0.70f;

			drawWindArc(consumer, matrix, relX, y2, relZ, r2, thick2, rot2, 1.35f, r, g, b, alpha2, 12);
			drawWindArc(consumer, matrix, relX, y2, relZ, r2, thick2, rot2 + 2.094f, 1.35f, r, g, b, alpha2, 12);
			drawWindArc(consumer, matrix, relX, y2, relZ, r2, thick2, rot2 + 4.188f, 1.35f, r, g, b, alpha2, 12);

			float r3 = woosh.size * (0.35f + 0.75f * easeOut);
			float thick3 = woosh.size * 0.08f * (1.0f - 0.3f * progress);
			float rot3 = elapsed * woosh.speed * 0.30f;
			float y3 = relY + 0.08f;
			float alpha3 = alphaFade * 0.55f;

			drawWindArc(consumer, matrix, relX, y3, relZ, r3, thick3, rot3, 1.57f, r, g, b, alpha3, 10);
			drawWindArc(consumer, matrix, relX, y3, relZ, r3, thick3, rot3 + 1.571f, 1.57f, r, g, b, alpha3, 10);
			drawWindArc(consumer, matrix, relX, y3, relZ, r3, thick3, rot3 + 3.142f, 1.57f, r, g, b, alpha3, 10);
			drawWindArc(consumer, matrix, relX, y3, relZ, r3, thick3, rot3 + 4.712f, 1.57f, r, g, b, alpha3, 10);
		}

		bufferSource.endBatch(RenderTypes.debugQuads());
	}

	private static void drawWindArc(
		VertexConsumer consumer,
		Matrix4f matrix,
		float cx,
		float cy,
		float cz,
		float radius,
		float thickness,
		float startAngle,
		float arcAngle,
		int r,
		int g,
		int b,
		float maxAlpha,
		int steps
	) {
		if (steps < 2 || maxAlpha <= 0.01f || radius <= 0.01f) {
			return;
		}

		for (int i = 0; i < steps; i++) {
			float f0 = (float) i / steps;
			float f1 = (float) (i + 1) / steps;

			float a0 = startAngle + f0 * arcAngle;
			float a1 = startAngle + f1 * arcAngle;

			float taper0 = (float) Math.sin(f0 * Math.PI);
			float taper1 = (float) Math.sin(f1 * Math.PI);

			float halfThick0 = thickness * 0.5f * (0.25f + 0.75f * taper0);
			float halfThick1 = thickness * 0.5f * (0.25f + 0.75f * taper1);

			float rIn0 = Math.max(0.01f, radius - halfThick0);
			float rOut0 = radius + halfThick0;

			float rIn1 = Math.max(0.01f, radius - halfThick1);
			float rOut1 = radius + halfThick1;

			float cos0 = Mth.cos(a0);
			float sin0 = Mth.sin(a0);
			float cos1 = Mth.cos(a1);
			float sin1 = Mth.sin(a1);

			float xIn0 = cx + cos0 * rIn0;
			float zIn0 = cz + sin0 * rIn0;
			float xOut0 = cx + cos0 * rOut0;
			float zOut0 = cz + sin0 * rOut0;

			float xIn1 = cx + cos1 * rIn1;
			float zIn1 = cz + sin1 * rIn1;
			float xOut1 = cx + cos1 * rOut1;
			float zOut1 = cz + sin1 * rOut1;

			float segAlpha0 = maxAlpha * (1.0f - f0 * 0.55f);
			float segAlpha1 = maxAlpha * (1.0f - f1 * 0.55f);
			int alphaInt0 = Mth.clamp((int) (segAlpha0 * 255.0f), 0, 255);
			int alphaInt1 = Mth.clamp((int) (segAlpha1 * 255.0f), 0, 255);
			int avgAlpha = (alphaInt0 + alphaInt1) / 2;

			int color = (avgAlpha << 24) | (r << 16) | (g << 8) | b;

			consumer.addVertex(matrix, xIn0, cy, zIn0).setColor(color);
			consumer.addVertex(matrix, xIn1, cy, zIn1).setColor(color);
			consumer.addVertex(matrix, xOut1, cy, zOut1).setColor(color);
			consumer.addVertex(matrix, xOut0, cy, zOut0).setColor(color);

			consumer.addVertex(matrix, xOut0, cy, zOut0).setColor(color);
			consumer.addVertex(matrix, xOut1, cy, zOut1).setColor(color);
			consumer.addVertex(matrix, xIn1, cy, zIn1).setColor(color);
			consumer.addVertex(matrix, xIn0, cy, zIn0).setColor(color);
		}
	}
}
