package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.mcreator.minigames.client.model.Modelshieldagent;
import net.mcreator.minigames.client.model.animations.shieldagentAnimation;
import net.mcreator.minigames.entity.ShieldAngelEntity;

import java.util.Map;

public class ShieldAngelRenderer extends MobRenderer<ShieldAngelEntity, ShieldAngelRenderer.ShieldAngelRenderState, ShieldAngelRenderer.AnimatedModel> {
	private static final Identifier DEFAULT_TEXTURE = Identifier.parse("minigames:textures/entities/empty.png");

	public ShieldAngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelshieldagent.LAYER_LOCATION)), 0.5f);

		this.addLayer(new RenderLayer<>(this) {
			private static final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/shieldangel.png");
			private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ShieldAngelRenderState state, float headYaw, float headPitch) {
				submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
			}
		});

		this.addLayer(new RenderLayer<>(this) {
			private static final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/shieldangel_emissive.png");
			private static final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ShieldAngelRenderState state, float headYaw, float headPitch) {
				submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
			}
		});
	}

	public static class ShieldAngelRenderState extends LivingEntityRenderState {
		public final AnimationState animationState0 = new AnimationState();
		public Identifier texture = DEFAULT_TEXTURE;
	}

	@Override
	public ShieldAngelRenderState createRenderState() {
		return new ShieldAngelRenderState();
	}

	@Override
	public void extractRenderState(ShieldAngelEntity entity, ShieldAngelRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.animationState0.copyFrom(entity.animationState0);

		if (entity.getTexture() != null && !entity.getTexture().equals("empty")) {
			state.texture = Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		} else {
			state.texture = DEFAULT_TEXTURE;
		}
	}

	@Override
	public Identifier getTextureLocation(ShieldAngelRenderState state) {
		return state.texture;
	}

	@Override
	protected void scale(ShieldAngelRenderState state, PoseStack poseStack) {
		poseStack.scale(0.9f, 0.9f, 0.9f);
	}

	public static class AnimatedModel extends Modelshieldagent {
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

		@Override
		public void setupAnim(LivingEntityRenderState state) {
			super.setupAnim(state);

			this.root().getAllParts().forEach(ModelPart::resetPose);

			if (state instanceof ShieldAngelRenderState customState) {
				this.keyframeAnimation0.apply(
						customState.animationState0,
						state.ageInTicks,
						1.0f
				);
			}
		}
	}
}