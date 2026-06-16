package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.mcreator.minigames.ModDataAttachments;
import net.mcreator.minigames.entity.ShieldAngelEntity;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class BlessedLinkRenderer {
	private static final String BLESSER_RUNTIME_TAG = "minigames:blesser";
	private static final TagKey<EntityType<?>> BLESSER_TYPE_TAG_MINIGAMES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("minigames", "blesser"));
	private static final TagKey<EntityType<?>> BLESSER_TYPE_TAG_MOD = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("mod", "blesser"));
	private static final double SEARCH_RADIUS = 30.0D;
	private static final float BEAM_HALF_WIDTH = 0.1F;
	private static final float BEAM_ALPHA = 0.35F;
	private static final float BEAM_DEPTH_BIAS = 0.0025F;

	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent.AfterEntities event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) return;
		if (mc.player == null || (mc.player.tickCount & 1) != 0) return;

		Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer beamBuffer = buffers.getBuffer(RenderType.debugQuads());

		List<LivingEntity> blessedEntities = mc.level.getEntitiesOfClass(LivingEntity.class, new AABB(cameraPos, cameraPos).inflate(SEARCH_RADIUS));
		List<Entity> allEntities = mc.level.getEntities((Entity) null, new AABB(cameraPos, cameraPos).inflate(SEARCH_RADIUS), e -> e != null && e.isAlive());
		for (LivingEntity blessed : blessedEntities) {
			if (!hasBlessedMarker(blessed)) continue;

			Entity blesser = findClosestBlesser(blessed, allEntities);
			if (blesser == null) continue;

			Vec3 start = new Vec3(blessed.getX(), blessed.getY() + (blessed.getBbHeight() * 0.5D), blessed.getZ()).subtract(cameraPos);
			Vec3 end = new Vec3(blesser.getX(), blesser.getY() + 1D, blesser.getZ()).subtract(cameraPos);
			drawQuadBeam(beamBuffer, start, end, cameraPos, 0.45F, 0.9F, 1.0F, BEAM_ALPHA, BEAM_HALF_WIDTH);
		}

		buffers.endBatch(RenderType.debugQuads());
	}

	private static boolean hasBlessedMarker(LivingEntity entity) {
		if (!entity.hasCustomName()) return false;
		String name = entity.getCustomName().getString();
		return name != null && name.contains("[blessed]");
	}

	private static Entity findClosestBlesser(LivingEntity source, List<Entity> candidates) {
		Entity closest = null;
		double bestDistanceSq = Double.MAX_VALUE;
		int sourceId = source.hasData(ModDataAttachments.BLESSED_DATA) ? source.getData(ModDataAttachments.BLESSED_DATA).dataId : 0;
		if (sourceId == 0) return null;
		for (Entity candidate : candidates) {
			if (candidate == source || !candidate.isAlive()) continue;
			if (!isBlesser(candidate)) continue;
			if (getBlesserDataId(candidate) != sourceId) continue;
			double distanceSq = source.distanceToSqr(candidate);
			if (distanceSq < bestDistanceSq) {
				bestDistanceSq = distanceSq;
				closest = candidate;
			}
		}
		return closest;
	}

	private static void drawQuadBeam(VertexConsumer consumer, Vec3 start, Vec3 end, Vec3 cameraWorldPos, float r, float g, float b, float a, float halfWidth) {
		Vec3 dir = end.subtract(start);
		if (dir.lengthSqr() < 1.0E-6D) return;
		dir = dir.normalize();

		Vec3 midWorld = start.add(end).scale(0.5D).add(cameraWorldPos);
		Vec3 toCamera = cameraWorldPos.subtract(midWorld);
		if (toCamera.lengthSqr() < 1.0E-6D) toCamera = new Vec3(0.0D, 1.0D, 0.0D);
		toCamera = toCamera.normalize();

		Vec3 right = dir.cross(toCamera);
		if (right.lengthSqr() < 1.0E-6D) right = dir.cross(new Vec3(0.0D, 1.0D, 0.0D));
		if (right.lengthSqr() < 1.0E-6D) right = new Vec3(1.0D, 0.0D, 0.0D);
		right = right.normalize().scale(halfWidth);

		Vec3 bias = toCamera.scale(BEAM_DEPTH_BIAS);
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

	private static boolean isBlesser(Entity entity) {
		return entity.getTags().contains(BLESSER_RUNTIME_TAG)
			|| entity.getType().is(BLESSER_TYPE_TAG_MINIGAMES)
			|| entity.getType().is(BLESSER_TYPE_TAG_MOD);
	}

	private static int getBlesserDataId(Entity entity) {
		if (entity instanceof ShieldAngelEntity shieldAngel) {
			return shieldAngel.getEntityData().get(ShieldAngelEntity.DATA_ID);
		}
		return entity.getPersistentData().getIntOr("DataID", 0);
	}
}
