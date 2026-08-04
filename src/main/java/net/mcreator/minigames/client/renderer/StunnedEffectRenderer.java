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
import net.mcreator.minigames.client.model.Modelstunned;
import net.mcreator.minigames.client.model.animations.worshipperAnimation;
import net.mcreator.minigames.entity.StunnedEffectEntity;

import java.util.Map;

public class StunnedEffectRenderer extends MobRenderer<StunnedEffectEntity, StunnedEffectRenderer.StunnedEffectRenderState, StunnedEffectRenderer.AnimatedModel> {
	private static final Identifier DEFAULT_TEXTURE = Identifier.parse("minigames:textures/entities/empty.png");

	public StunnedEffectRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelstunned.LAYER_LOCATION)), 0.5f);

		this.addLayer(new RenderLayer<>(this) {
			private static final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/stunned.png");
			private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, StunnedEffectRenderState state, float limbSwing, float limbSwingAmount) {
				submitNodeCollector.submitModel(
						this.getParentModel(),
						state,
						poseStack,
						RENDER_TYPE,
						light,
						OverlayTexture.NO_OVERLAY,
						state.outlineColor,
						null
				);
			}
		});
	}

	public static class StunnedEffectRenderState extends LivingEntityRenderState {
		public final AnimationState animationState0 = new AnimationState();
		public Identifier texture = DEFAULT_TEXTURE;
	}

	@Override
	public StunnedEffectRenderState createRenderState() {
		return new StunnedEffectRenderState();
	}

	@Override
	public void extractRenderState(StunnedEffectEntity entity, StunnedEffectRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);

		state.animationState0.copyFrom(entity.animationState0);

		if (entity.getTexture() != null && !entity.getTexture().equals("empty")) {
			state.texture = Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		} else {
			state.texture = DEFAULT_TEXTURE;
		}
	}

	@Override
	public Identifier getTextureLocation(StunnedEffectRenderState state) {
		return state.texture;
	}

	public static class AnimatedModel extends Modelstunned {
		private final KeyframeAnimation keyframeAnimation0;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(worshipperAnimation.idle);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(this.root());
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(this.root());
			}
		}

		@Override
		public void setupAnim(LivingEntityRenderState state) {
			super.setupAnim(state);

			this.root().getAllParts().forEach(ModelPart::resetPose);

			if (state instanceof StunnedEffectRenderState customState) {
				this.keyframeAnimation0.apply(
						customState.animationState0,
						state.ageInTicks,
						1.0f
				);
			}
		}
	}
}