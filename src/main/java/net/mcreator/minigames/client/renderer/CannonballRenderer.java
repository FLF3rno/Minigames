package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;

import net.mcreator.minigames.entity.CannonballEntity;
import net.mcreator.minigames.client.model.Modelcannonball;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class CannonballRenderer extends EntityRenderer<CannonballEntity, LivingEntityRenderState> {

	private static final Identifier TEXTURE =
			Identifier.parse("minigames:textures/entities/cannonball.png");

	private final Modelcannonball model;

	public CannonballRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new Modelcannonball(
				context.bakeLayer(Modelcannonball.LAYER_LOCATION)
		);
	}

	@Override
	public void submit(
			LivingEntityRenderState state,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			CameraRenderState camera
	) {
		poseStack.pushPose();
		poseStack.translate(0.0D, -2.7D, 0.0D);
		poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot));
		poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));

		model.setupAnim(state);

		submitNodeCollector.submitModel(
				model,
				state,
				poseStack,
				TEXTURE,
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				state.outlineColor,
				null
		);

		poseStack.popPose();

		super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(
			CannonballEntity entity,
			LivingEntityRenderState state,
			float partialTicks
	) {
		super.extractRenderState(entity, state, partialTicks);

		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}