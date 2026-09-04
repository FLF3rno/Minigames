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

import net.mcreator.minigames.procedures.AnimationWalkingProcedure;
import net.mcreator.minigames.entity.GravediggerMinibossEntity;
import net.mcreator.minigames.client.model.animations.gravediggerAnimation;
import net.mcreator.minigames.client.model.animations.candleheadAnimation;
import net.mcreator.minigames.client.model.Modelgravedigger;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class GravediggerMinibossRenderer extends MobRenderer<GravediggerMinibossEntity, LivingEntityRenderState, Modelgravedigger> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/gravedigger.png");

	public GravediggerMinibossRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelgravedigger.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/gravedigger_emissive.png");
			final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);
			final EntityModel LAYER_MODEL = new Modelgravedigger(Minecraft.getInstance().getEntityModels().bakeLayer(Modelgravedigger.LAYER_LOCATION));

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				LAYER_MODEL.setupAnim(state);
				submitNodeCollector.submitModel(LAYER_MODEL, state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(GravediggerMinibossEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		GravediggerMinibossEntity entity = (GravediggerMinibossEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "gravedigger")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(1.3f, 1.3f, 1.3f);
	}

	private static final class AnimatedModel extends Modelgravedigger {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;
		private final KeyframeAnimation keyframeAnimation2;
		private final KeyframeAnimation keyframeAnimation3;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(gravediggerAnimation.walk);
			this.keyframeAnimation1 = safeBake(candleheadAnimation.attack);
			this.keyframeAnimation2 = safeBake(gravediggerAnimation.dig);
			this.keyframeAnimation3 = safeBake(gravediggerAnimation.summon);
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
			GravediggerMinibossEntity entity = state.getRenderData(ENTITY_KEY);
			if (AnimationWalkingProcedure.execute(entity))
				this.keyframeAnimation0.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1f, 1f);
			this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
			this.keyframeAnimation2.apply(entity.animationState2, state.ageInTicks, 1f);
			this.keyframeAnimation3.apply(entity.animationState3, state.ageInTicks, 1f);
			super.setupAnim(state);
		}
	}

	public static final ContextKey<GravediggerMinibossEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:gravedigger_miniboss_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(GravediggerMinibossRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}