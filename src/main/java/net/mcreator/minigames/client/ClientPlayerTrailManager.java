package net.mcreator.minigames.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class ClientPlayerTrailManager {

	private static final List<TrailEmitter> ACTIVE_EMITTERS = new CopyOnWriteArrayList<>();
	private static final List<CloneSnapshot> ACTIVE_CLONES = new CopyOnWriteArrayList<>();
	private static PlayerModel playerModelWide = null;
	private static PlayerModel playerModelSlim = null;

	public static class PartSnapshot {
		final float x, y, z;
		final float xRot, yRot, zRot;
		final float xScale, yScale, zScale;
		final boolean visible;

		PartSnapshot(ModelPart part) {
			this.x = part.x;
			this.y = part.y;
			this.z = part.z;
			this.xRot = part.xRot;
			this.yRot = part.yRot;
			this.zRot = part.zRot;
			this.xScale = part.xScale;
			this.yScale = part.yScale;
			this.zScale = part.zScale;
			this.visible = part.visible;
		}

		void applyTo(ModelPart part) {
			part.x = this.x;
			part.y = this.y;
			part.z = this.z;
			part.xRot = this.xRot;
			part.yRot = this.yRot;
			part.zRot = this.zRot;
			part.xScale = this.xScale;
			part.yScale = this.yScale;
			part.zScale = this.zScale;
			part.visible = this.visible;
		}
	}

	public static class TrailEmitter {
		final int entityId;
		final boolean fadeOut;
		final int cloneDuration;
		final float opacity;
		int ticksRemaining;
		final String color;
		int stepIndex;
		double lastSpawnX;
		double lastSpawnY;
		double lastSpawnZ;

		TrailEmitter(int entityId, boolean fadeOut, int cloneDuration, float opacity, int ticksRemaining, String color) {
			this.entityId = entityId;
			this.fadeOut = fadeOut;
			this.cloneDuration = Math.max(1, cloneDuration);
			this.opacity = opacity;
			this.ticksRemaining = Math.max(1, ticksRemaining);
			this.color = color != null ? color : "";
			this.stepIndex = 0;
			this.lastSpawnX = Double.NaN;
			this.lastSpawnY = Double.NaN;
			this.lastSpawnZ = Double.NaN;
		}
	}

	public static class CloneSnapshot {
		final int playerId;
		final double x, y, z;
		final Matrix4f rotationMatrix;
		final Identifier skin;
		final boolean isSlim;
		final int totalDuration;
		int remainingTicks;
		final float baseOpacity;
		final boolean fadeOut;
		final int color;
		final int lightCoords;

		PartSnapshot head, hat, body, jacket;
		PartSnapshot leftArm, leftSleeve, rightArm, rightSleeve;
		PartSnapshot leftLeg, leftPants, rightLeg, rightPants;

		CloneSnapshot(int playerId, double x, double y, double z, Matrix4f rotationMatrix, Identifier skin, boolean isSlim, int totalDuration, float baseOpacity, boolean fadeOut, int color, int lightCoords) {
			this.playerId = playerId;
			this.x = x;
			this.y = y;
			this.z = z;
			this.rotationMatrix = rotationMatrix;
			this.skin = skin;
			this.isSlim = isSlim;
			this.totalDuration = Math.max(1, totalDuration);
			this.remainingTicks = this.totalDuration;
			this.baseOpacity = baseOpacity;
			this.fadeOut = fadeOut;
			this.color = color;
			this.lightCoords = lightCoords;
		}

		void snapshotModel(PlayerModel model) {
			this.head = new PartSnapshot(model.head);
			this.hat = new PartSnapshot(model.hat);
			this.body = new PartSnapshot(model.body);
			this.jacket = new PartSnapshot(model.jacket);
			this.leftArm = new PartSnapshot(model.leftArm);
			this.leftSleeve = new PartSnapshot(model.leftSleeve);
			this.rightArm = new PartSnapshot(model.rightArm);
			this.rightSleeve = new PartSnapshot(model.rightSleeve);
			this.leftLeg = new PartSnapshot(model.leftLeg);
			this.leftPants = new PartSnapshot(model.leftPants);
			this.rightLeg = new PartSnapshot(model.rightLeg);
			this.rightPants = new PartSnapshot(model.rightPants);
		}

		void applyTo(PlayerModel model) {
			if (this.head != null) this.head.applyTo(model.head);
			if (this.hat != null) this.hat.applyTo(model.hat);
			if (this.body != null) this.body.applyTo(model.body);
			if (this.jacket != null) this.jacket.applyTo(model.jacket);
			if (this.leftArm != null) this.leftArm.applyTo(model.leftArm);
			if (this.leftSleeve != null) this.leftSleeve.applyTo(model.leftSleeve);
			if (this.rightArm != null) this.rightArm.applyTo(model.rightArm);
			if (this.rightSleeve != null) this.rightSleeve.applyTo(model.rightSleeve);
			if (this.leftLeg != null) this.leftLeg.applyTo(model.leftLeg);
			if (this.leftPants != null) this.leftPants.applyTo(model.leftPants);
			if (this.rightLeg != null) this.rightLeg.applyTo(model.rightLeg);
			if (this.rightPants != null) this.rightPants.applyTo(model.rightPants);
		}
	}

	public static void startTrail(int entityId, boolean fadeOut, double cloneDuration, double opacity, double ticks, String color) {
		float effectiveOpacity = opacity > 1.0 ? (float) (opacity / 100.0) : (float) opacity;
		effectiveOpacity = Mth.clamp(effectiveOpacity, 0.05f, 1.0f);
		int duration = Math.max(1, (int) Math.round(cloneDuration));
		int totalTicks = Math.max(1, (int) Math.round(ticks));

		ACTIVE_EMITTERS.removeIf(e -> e.entityId == entityId);
		TrailEmitter emitter = new TrailEmitter(entityId, fadeOut, duration, effectiveOpacity, totalTicks, color);
		ACTIVE_EMITTERS.add(emitter);
		emitClone(emitter);
	}

	private static void emitClone(TrailEmitter emitter) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		Entity entity = mc.level.getEntity(emitter.entityId);
		if (!(entity instanceof AbstractClientPlayer player)) return;

		int cloneColor;
		if (emitter.color.equalsIgnoreCase("rainbow")) {
			float hue = (float) (((emitter.stepIndex * 0.12) + ((System.currentTimeMillis() % 3000) / 3000.0)) % 1.0);
			cloneColor = java.awt.Color.HSBtoRGB(hue, 0.85f, 1.0f) & 0xFFFFFF;
		} else {
			cloneColor = ClientGlowManager.parseColor(emitter.color);
		}
		emitter.stepIndex++;

		boolean isSlim = player.getSkin().model() == PlayerModelType.SLIM;
		Identifier skin = player.getSkin().body().texturePath();

		Vec3 look = player.getLookAngle();
		double backwardOffset = 1.2;
		double spawnX = player.getX() - look.x * backwardOffset;
		double spawnY = player.getY() - look.y * backwardOffset;
		double spawnZ = player.getZ() - look.z * backwardOffset;

		if (!Double.isNaN(emitter.lastSpawnX)) {
			double ddx = spawnX - emitter.lastSpawnX;
			double ddy = spawnY - emitter.lastSpawnY;
			double ddz = spawnZ - emitter.lastSpawnZ;
			if (ddx * ddx + ddy * ddy + ddz * ddz < 0.04) {
				return;
			}
		}
		emitter.lastSpawnX = spawnX;
		emitter.lastSpawnY = spawnY;
		emitter.lastSpawnZ = spawnZ;

		Matrix4f rotMatrix = new Matrix4f();
		if (mc.getEntityRenderDispatcher().getRenderer(player) instanceof AvatarRenderer renderer) {
			AvatarRenderState state = (AvatarRenderState) renderer.createRenderState(player, 1.0F);
			int lightCoords = state.lightCoords;
			PlayerModel animModel = (PlayerModel) renderer.getModel();
			animModel.setupAnim(state);

			PoseStack rotStack = new PoseStack();
			renderer.setupRotations(state, rotStack, state.bodyRot, 69);
			rotMatrix = new Matrix4f(rotStack.last().pose());

			CloneSnapshot clone = new CloneSnapshot(
					player.getId(),
					spawnX, spawnY, spawnZ,
					rotMatrix, skin, isSlim, emitter.cloneDuration,
					emitter.opacity, emitter.fadeOut, cloneColor, lightCoords
			);
			clone.snapshotModel(animModel);
			ACTIVE_CLONES.add(clone);
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			ACTIVE_EMITTERS.clear();
			ACTIVE_CLONES.clear();
			return;
		}

		for (TrailEmitter emitter : ACTIVE_EMITTERS) {
			emitter.ticksRemaining--;
			if (emitter.ticksRemaining > 0) {
				emitClone(emitter);
			} else {
				ACTIVE_EMITTERS.remove(emitter);
			}
		}

		for (CloneSnapshot clone : ACTIVE_CLONES) {
			clone.remainingTicks--;
			if (clone.remainingTicks <= 0) {
				ACTIVE_CLONES.remove(clone);
			}
		}
	}

	private static PlayerModel getWideModel() {
		if (playerModelWide == null) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.getEntityModels() != null) {
				playerModelWide = new PlayerModel(mc.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
			}
		}
		return playerModelWide;
	}

	private static PlayerModel getSlimModel() {
		if (playerModelSlim == null) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.getEntityModels() != null) {
				playerModelSlim = new PlayerModel(mc.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
			}
		}
		return playerModelSlim;
	}

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent.AfterTranslucentBlocks event) {
		if (ACTIVE_CLONES.isEmpty()) return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) return;

		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		Vec3 camera = mc.gameRenderer.getMainCamera().position();
		float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

		for (CloneSnapshot clone : ACTIVE_CLONES) {
			if (mc.player != null && clone.playerId == mc.player.getId() && mc.options.getCameraType().isFirstPerson()) {
				double dx = clone.x - camera.x;
				double dy = clone.y - camera.y;
				double dz = clone.z - camera.z;
				if (dx * dx + dy * dy + dz * dz < 4.0) {
					continue;
				}
				Vec3 camLook = mc.player.getLookAngle();
				double dot = dx * camLook.x + dy * camLook.y + dz * camLook.z;
				if (dot > -0.5) {
					continue;
				}
			}
			float elapsed = (clone.totalDuration - clone.remainingTicks) + partialTick;
			float progress = Mth.clamp(elapsed / (float) clone.totalDuration, 0.0f, 1.0f);
			float alpha = clone.baseOpacity;
			if (clone.fadeOut) {
				alpha = clone.baseOpacity * (1.0f - progress);
			}
			if (alpha <= 0.01f) continue;
			int alphaInt = (int) (alpha * 255.0f);
			int argb = (alphaInt << 24) | (clone.color & 0xFFFFFF);

			poseStack.pushPose();
			poseStack.translate(clone.x - camera.x, clone.y - camera.y, clone.z - camera.z);
			poseStack.mulPose(clone.rotationMatrix);
			poseStack.scale(-0.938f, -0.938f, 0.938f);
			poseStack.translate(0.0D, -1.501D, 0.0D);

			PlayerModel model = clone.isSlim ? getSlimModel() : getWideModel();
			if (model != null) {
				clone.applyTo(model);
				RenderType renderType = RenderTypes.entityTranslucent(clone.skin);
				VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
				model.renderToBuffer(poseStack, vertexConsumer, clone.lightCoords, OverlayTexture.NO_OVERLAY, argb);
			}

			poseStack.popPose();
		}
	}
}
