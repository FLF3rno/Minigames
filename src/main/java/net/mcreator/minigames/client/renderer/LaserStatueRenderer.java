package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.client.model.animations.laser_statueAnimation;
import net.mcreator.minigames.client.model.Modellaser_statue;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class LaserStatueRenderer extends MobRenderer<LaserStatueEntity, LivingEntityRenderState, Modellaser_statue> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/laser_statue.png");

	public LaserStatueRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modellaser_statue.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/laser_statue_emissive.png");
			final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(LaserStatueEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		LaserStatueEntity entity = (LaserStatueEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "laser_statue")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modellaser_statue {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(laser_statueAnimation.active);
			this.keyframeAnimation1 = safeBake(laser_statueAnimation.damage);
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
			LaserStatueEntity entity = state.getRenderData(ENTITY_KEY);
			if (entity != null) {
				if (entity.animationState0.isStarted())
					this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
				if (entity.animationState1.isStarted())
					this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
			}
			super.setupAnim(state);
		}
	}

	public static final ContextKey<LaserStatueEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:laser_statue_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(LaserStatueRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}