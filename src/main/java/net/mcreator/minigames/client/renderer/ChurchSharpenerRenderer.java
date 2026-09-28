package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.minigames.procedures.AnimationWalkingProcedure;
import net.mcreator.minigames.entity.ChurchSharpenerEntity;
import net.mcreator.minigames.client.model.animations.church_sharpenerAnimation;
import net.mcreator.minigames.client.model.Modelchurch_sharpener;

import java.util.Map;

public class ChurchSharpenerRenderer extends MobRenderer<ChurchSharpenerEntity, LivingEntityRenderState, Modelchurch_sharpener> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/church_sharpener.png");

	public ChurchSharpenerRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelchurch_sharpener.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(ChurchSharpenerEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		ChurchSharpenerEntity entity = (ChurchSharpenerEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "church_sharpener")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelchurch_sharpener {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;
		private final KeyframeAnimation keyframeAnimation2;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(church_sharpenerAnimation.sharpening);
			this.keyframeAnimation1 = safeBake(church_sharpenerAnimation.chase);
			this.keyframeAnimation2 = safeBake(church_sharpenerAnimation.stun);
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
			this.root().getAllParts().forEach(ModelPart::resetPose);
			ChurchSharpenerEntity entity = state.getRenderData(ENTITY_KEY);
			if (entity != null) {
				this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
				this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
				if (AnimationWalkingProcedure.execute(entity))
					this.keyframeAnimation1.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.2f, 1f);
				this.keyframeAnimation2.apply(entity.animationState2, state.ageInTicks, 1f);
			}
			super.setupAnim(state);
		}
	}

	public static final ContextKey<ChurchSharpenerEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:church_sharpener_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(ChurchSharpenerRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}