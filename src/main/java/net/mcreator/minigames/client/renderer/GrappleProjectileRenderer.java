package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.mcreator.minigames.client.model.Modelgrapple;
import net.mcreator.minigames.entity.GrappleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class GrappleProjectileRenderer extends EntityRenderer<GrappleEntity, GrappleProjectileRenderer.GrappleRenderState> {
	private static final Identifier TEXTURE = Identifier.parse("minigames:textures/entities/grapple.png");
	private final Modelgrapple model;

	public GrappleProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new Modelgrapple(context.bakeLayer(Modelgrapple.LAYER_LOCATION));
	}

	public static class GrappleRenderState extends LivingEntityRenderState {
		public GrappleEntity grapple;
		public Vec3 grapplePos;
		public Vec3 ownerPos;
	}

	@Override
	public GrappleRenderState createRenderState() {
		return new GrappleRenderState();
	}

	@Override
	public void extractRenderState(GrappleEntity entity, GrappleRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.grapple = entity;
		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
		state.grapplePos = new Vec3(
				Mth.lerp(partialTicks, entity.xo, entity.getX()),
				Mth.lerp(partialTicks, entity.yo, entity.getY()),
				Mth.lerp(partialTicks, entity.zo, entity.getZ())
		);
		Entity owner = entity.getOwner();
		if (owner != null) {
			state.ownerPos = new Vec3(
					Mth.lerp(partialTicks, owner.xo, owner.getX()),
					Mth.lerp(partialTicks, owner.yo, owner.getY()),
					Mth.lerp(partialTicks, owner.zo, owner.getZ())
			);
		} else {
			state.ownerPos = null;
		}
	}

	@Override
	public void submit(GrappleRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
		GrappleEntity entity = state.grapple;

		poseStack.pushPose();
		poseStack.translate(0.0D, -1.27D, 0.0D);
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));

		model.setupAnim(state);
		nodeCollector.submitModel(
				model,
				state,
				poseStack,
				RenderTypes.entityCutout(TEXTURE),
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0xFFFFFFFF,
				null,
				state.outlineColor,
				null
		);

		if (entity != null && state.ownerPos != null && entity.getOwner() != null && entity.getOwner().isAlive()) {
			renderRope(state);
		}

		poseStack.popPose();
		super.submit(state, poseStack, nodeCollector, cameraRenderState);
	}

	private void renderRope(GrappleRenderState state) {
		GrappleEntity grapple = state.grapple;
		if (grapple == null || state.grapplePos == null || state.ownerPos == null) {
			return;
		}

		Vec3 cameraPos = this.entityRenderDispatcher.camera.position();

		Vec3 startWorld = state.grapplePos.add(0.0D, grapple.getBbHeight() * 0.98D, 0.0D);
		Vec3 endWorld = state.ownerPos.add(0.0D, 0.46D, 0.0D);
		Vec3 delta = endWorld.subtract(startWorld);
		double length = delta.length();
		if (length < 1.0E-4D) {
			return;
		}

		Vec3 forward = delta.normalize();
		Vec3 sideways = forward.cross(new Vec3(0.0D, 1.0D, 0.0D));
		if (sideways.lengthSqr() < 1.0E-6D) {
			sideways = forward.cross(new Vec3(1.0D, 0.0D, 0.0D));
		}
		sideways = sideways.normalize().scale(0.06D);

		PoseStack ropePose = new PoseStack();
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		VertexConsumer consumer = bufferSource.getBuffer(RenderTypes.debugQuads());
		Matrix4f matrix = ropePose.last().pose();
		int light = 15728880;
		int segments = Math.max(4, (int) (length / 0.25D));

		for (int i = 1; i <= segments; i++) {
			double t0 = (double) (i - 1) / segments;
			double t1 = (double) i / segments;
			Vec3 prev = getLeashPoint(startWorld, endWorld, t0).subtract(cameraPos);
			Vec3 center = getLeashPoint(startWorld, endWorld, t1).subtract(cameraPos);
			Vec3 segmentSide = sideways.scale(1.0D + 0.08D * Math.sin(t1 * Math.PI * 6.0D));
			drawRibbonSegment(matrix, consumer, prev, center, segmentSide, light, t1);
		}

		bufferSource.endBatch(RenderTypes.debugQuads());
	}

	private static Vec3 getLeashPoint(Vec3 start, Vec3 end, double t) {
		return start.lerp(end, t);
	}

	private static void drawRibbonSegment(Matrix4f matrix, VertexConsumer consumer, Vec3 start, Vec3 end, Vec3 side, int light, double t) {
		int band = ((int) Math.floor(t * 8.0D)) & 1;
		int r = band == 0 ? 154 : 118;
		int g = band == 0 ? 108 : 86;
		int b = band == 0 ? 66 : 52;
		Vec3 startRight = start.add(side);
		Vec3 startLeft = start.subtract(side);
		Vec3 endRight = end.add(side);
		Vec3 endLeft = end.subtract(side);

		consumer.addVertex(matrix, (float) startRight.x, (float) startRight.y, (float) startRight.z).setColor(r, g, b, 255).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix, (float) endRight.x, (float) endRight.y, (float) endRight.z).setColor(r, g, b, 255).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix, (float) endLeft.x, (float) endLeft.y, (float) endLeft.z).setColor(r, g, b, 255).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
		consumer.addVertex(matrix, (float) startLeft.x, (float) startLeft.y, (float) startLeft.z).setColor(r, g, b, 255).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
	}
}
