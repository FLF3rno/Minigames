package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.AnimationState;

import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.client.model.animations.shieldagentAnimation;
import net.mcreator.minigames.client.model.Modelshieldagent;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ShieldAngelRenderer extends MobRenderer<ShieldAngelEntity, LivingEntityRenderState, Modelshieldagent> {
	private ShieldAngelEntity entity = null;
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/empty.png");

	public ShieldAngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelshieldagent.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minigames:textures/entities/shieldangel.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(LAYER_TEXTURE));
				EntityModel<LivingEntityRenderState> model = this.getParentModel();
				model.setupAnim(state);
				model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
			}
		});
		this.addLayer(new RenderLayer<>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minigames:textures/entities/shieldangel_emissive.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				EntityModel<LivingEntityRenderState> model = this.getParentModel();
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
	public void extractRenderState(ShieldAngelEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = entity;
		if (this.model instanceof AnimatedModel) {
			((AnimatedModel) this.model).setEntity(entity);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		if (entity != null && !"empty".equals(entity.getTexture()))
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.9f, 0.9f, 0.9f);
	}

	private static final class AnimatedModel extends Modelshieldagent {
		private ShieldAngelEntity entity = null;
		private final KeyframeAnimation keyframeAnimation0;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(shieldagentAnimation.rotate);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(root);
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(root);
			}
		}

		public void setEntity(ShieldAngelEntity entity) {
			this.entity = entity;
		}

		@Override
		public void setupAnim(LivingEntityRenderState state) {
    		this.root().getAllParts().forEach(ModelPart::resetPose);

    		AnimationState alwaysOn = new AnimationState();
    		alwaysOn.start(0);
    		this.keyframeAnimation0.apply(alwaysOn, state.ageInTicks, 1.0f);

    		super.setupAnim(state);
		}
	}
}
