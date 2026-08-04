package net.mcreator.minigames.client.renderer;

import com.mojang.math.Axis;

import net.mcreator.minigames.client.model.Modelarrowmodel;
import net.mcreator.minigames.entity.BlessedArrowEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;


public class BlessedArrowRenderer extends EntityRenderer<BlessedArrowEntity, LivingEntityRenderState> {
	private static final Identifier TEXTURE =
			Identifier.parse("minigames:textures/entities/blessed_arrow.png");

	private final Modelarrowmodel model;

	public BlessedArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new Modelarrowmodel(context.bakeLayer(Modelarrowmodel.LAYER_LOCATION));
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

		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(90 + state.xRot));

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
	public void extractRenderState(BlessedArrowEntity entity,
								   LivingEntityRenderState state,
								   float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.xRot = entity.getXRot(partialTicks);
		state.yRot = entity.getYRot(partialTicks);
	}
}