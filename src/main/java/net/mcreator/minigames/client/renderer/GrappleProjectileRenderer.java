package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.minigames.entity.GrappleEntity;
import net.mcreator.minigames.client.model.Modelgrapple;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class GrappleProjectileRenderer extends EntityRenderer<GrappleEntity, LivingEntityRenderState> {
	private static final ResourceLocation TEXTURE = ResourceLocation.parse("minigames:textures/entities/grapple.png");
	private final Modelgrapple model;

	public GrappleProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new Modelgrapple(context.bakeLayer(Modelgrapple.LAYER_LOCATION));
	}

	@Override
	public void render(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(TEXTURE));
		poseStack.pushPose();
		poseStack.translate(0, -0.75, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
		model.setupAnim(state);
		model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
		super.render(state, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(GrappleEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}
