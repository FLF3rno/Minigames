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

import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.mcreator.minigames.client.model.animations.flavio_trapdoorAnimation;
import net.mcreator.minigames.client.model.Modelflavio_trapdoor;

import java.util.Map;

public class FlavioTrapdoorRenderer extends MobRenderer<FlavioTrapdoorEntity, LivingEntityRenderState, Modelflavio_trapdoor> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/flavio_trapdoor.png");

	public FlavioTrapdoorRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelflavio_trapdoor.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioTrapdoorEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioTrapdoorEntity entity = (FlavioTrapdoorEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "flavio_trapdoor")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelflavio_trapdoor {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(flavio_trapdoorAnimation.open);
			this.keyframeAnimation1 = safeBake(flavio_trapdoorAnimation.close);
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
			FlavioTrapdoorEntity entity = state.getRenderData(ENTITY_KEY);
			this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
			this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
			super.setupAnim(state);
		}
	}

	public static final ContextKey<FlavioTrapdoorEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_trapdoor_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioTrapdoorRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}