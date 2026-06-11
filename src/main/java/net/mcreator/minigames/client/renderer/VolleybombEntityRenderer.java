package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.entity.VolleybombEntityEntity;
import net.mcreator.minigames.client.model.Modelvolleybomb;
import net.mcreator.minigames.client.model.Modelpewseat;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class VolleybombEntityRenderer extends MobRenderer<VolleybombEntityEntity, LivingEntityRenderState, Modelpewseat> {
	private VolleybombEntityEntity entity = null;
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/empty.png");

	public VolleybombEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelpewseat(context.bakeLayer(Modelpewseat.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minigames:textures/entities/volleybomb.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(LAYER_TEXTURE));
				EntityModel model = new Modelvolleybomb(Minecraft.getInstance().getEntityModels().bakeLayer(Modelvolleybomb.LAYER_LOCATION));
				model.setupAnim(state);
				model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(VolleybombEntityEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = entity;
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		if (entity != null && entity.getTexture() != "empty")
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.9f, 0.9f, 0.9f);
	}
}