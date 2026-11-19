package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;

import net.mcreator.minigames.entity.BoulderEntity;
import net.mcreator.minigames.client.model.Modelrock;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class BoulderRenderer extends EntityRenderer<BoulderEntity, LivingEntityRenderState> {
	private static final Identifier texture = Identifier.parse("minigames:textures/entities/rock_thrower_church.png");
	private final Modelrock model;

	public BoulderRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new Modelrock(context.bakeLayer(Modelrock.LAYER_LOCATION));
	}

	@Override
	public void submit(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.rotateDegrees(Axis.YP, state.yRot - 90);
		poseStack.rotateDegrees(Axis.ZP, 90 + state.xRot);
		model.setupAnim(state);
		submitNodeCollector.submitModel(this.model, state, poseStack, texture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
		super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(BoulderEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}