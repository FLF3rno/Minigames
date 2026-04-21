package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;

import net.mcreator.minigames.entity.StunnedEffectEntity;
import net.mcreator.minigames.client.model.animations.stunnedAnimation;
import net.mcreator.minigames.client.model.Modelstunned;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class StunnedEffectRenderer extends MobRenderer<StunnedEffectEntity, StunnedEffectRenderer.StunnedEffectRenderState, Modelstunned> {
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/empty.png");

	public StunnedEffectRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelstunned.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<StunnedEffectRenderState, Modelstunned>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minigames:textures/entities/stunned.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, StunnedEffectRenderState state, float limbSwing, float limbSwingAmount) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(state, 0.0F), -1);
			}
		});
	}

	public static class StunnedEffectRenderState extends LivingEntityRenderState {
		public AnimationState animationState;
		public String texture = "stunned";
	}

	@Override
	public StunnedEffectRenderState createRenderState() {
		return new StunnedEffectRenderState();
	}

	@Override
	public void extractRenderState(StunnedEffectEntity entity, StunnedEffectRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.animationState = entity.animationState0;
		state.texture = entity.getTexture();
	}

	@Override
	public ResourceLocation getTextureLocation(StunnedEffectRenderState state) {
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelstunned {
		private final KeyframeAnimation keyframeAnimation0;
		private final ModelPart root;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.root = root;
			this.keyframeAnimation0 = safeBake(stunnedAnimation.idle);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(this.root);
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(this.root);
			}
		}

		@Override
		public void setupAnim(LivingEntityRenderState state) {
			super.setupAnim(state);
			this.root.getAllParts().forEach(ModelPart::resetPose);
			if (state instanceof StunnedEffectRenderState _state && _state.animationState != null) {
				this.keyframeAnimation0.apply(_state.animationState, state.ageInTicks, 1f);
			}
		}
	}
}
