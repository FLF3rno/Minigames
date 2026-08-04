package net.mcreator.minigames.client.renderer;

import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import com.mojang.blaze3d.vertex.PoseStack;

import net.mcreator.minigames.client.model.Modeldart;
import net.mcreator.minigames.entity.GlueProjectileEntity;

public class GlueProjectileRenderer extends EntityRenderer<GlueProjectileEntity, LivingEntityRenderState> {
	private static final Identifier TEXTURE =
			Identifier.parse("minigames:textures/entities/gluedartproj.png");

	private final Modeldart model;

	public GlueProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new Modeldart(context.bakeLayer(Modeldart.LAYER_LOCATION));
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void submit(LivingEntityRenderState state,
					   PoseStack poseStack,
					   SubmitNodeCollector nodeCollector,
					   CameraRenderState cameraRenderState) {

		poseStack.pushPose();

		poseStack.translate(0.0D, -0.75D, 0.0D);
		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 90.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));

		nodeCollector.submitModel(
				model,
				state,
				poseStack,
				RenderTypes.entityCutout(TEXTURE),
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				-1,
				null,
				state.outlineColor,
				null
		);

		poseStack.popPose();

		super.submit(state, poseStack, nodeCollector, cameraRenderState);
	}

	@Override
	public void extractRenderState(GlueProjectileEntity entity,
								   LivingEntityRenderState state,
								   float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}