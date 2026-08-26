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

import net.mcreator.minigames.entity.FlavioSweeperEntity;
import net.mcreator.minigames.client.model.animations.flavio_sweeperAnimation;
import net.mcreator.minigames.client.model.Modelflavio_sweeper;

import java.util.Map;

public class FlavioSweeperRenderer extends MobRenderer<FlavioSweeperEntity, LivingEntityRenderState, Modelflavio_sweeper> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/sweeper.png");

	public FlavioSweeperRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelflavio_sweeper.LAYER_LOCATION)), 0.5f);
	}
	@Override
	protected boolean affectedByCulling(FlavioSweeperEntity entity) {
		return false;
	}
	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioSweeperEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioSweeperEntity entity = (FlavioSweeperEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "sweeper")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelflavio_sweeper {
		private final KeyframeAnimation keyframeAnimation0;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(flavio_sweeperAnimation.spin);
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
			FlavioSweeperEntity entity = state.getRenderData(ENTITY_KEY);
			this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
			super.setupAnim(state);
		}
	}

	public static final ContextKey<FlavioSweeperEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_sweeper_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioSweeperRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}