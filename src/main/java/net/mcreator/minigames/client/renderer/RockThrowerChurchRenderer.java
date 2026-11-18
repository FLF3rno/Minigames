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

import net.mcreator.minigames.entity.RockThrowerChurchEntity;
import net.mcreator.minigames.client.model.animations.rock_thrower_churchAnimation;
import net.mcreator.minigames.client.model.Modelrock_thrower_church;

import java.util.Map;

public class RockThrowerChurchRenderer extends MobRenderer<RockThrowerChurchEntity, LivingEntityRenderState, Modelrock_thrower_church> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/rock_thrower_church.png");

	public RockThrowerChurchRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelrock_thrower_church.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(RockThrowerChurchEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		RockThrowerChurchEntity entity = (RockThrowerChurchEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "rock_thrower_church")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelrock_thrower_church {
		private final KeyframeAnimation keyframeAnimation0;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(rock_thrower_churchAnimation.rock_throw);
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
			RockThrowerChurchEntity entity = state.getRenderData(ENTITY_KEY);
			if (entity != null) {
				this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
			}
			super.setupAnim(state);
		}
	}

	public static final ContextKey<RockThrowerChurchEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:rock_thrower_church_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(RockThrowerChurchRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}