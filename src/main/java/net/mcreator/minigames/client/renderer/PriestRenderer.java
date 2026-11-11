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
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.minigames.entity.PriestEntity;
import net.mcreator.minigames.client.model.animations.priestAnimation;
import net.mcreator.minigames.client.model.Modelpriest;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class PriestRenderer extends MobRenderer<PriestEntity, LivingEntityRenderState, Modelpriest> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/priest.png");

	public PriestRenderer(EntityRendererProvider.Context context) {
		super(context, new AnimatedModel(context.bakeLayer(Modelpriest.LAYER_LOCATION)), 0f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/priest_emissive.png");
			final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
			}
		});
	}

	@Override

	public boolean shouldRender(PriestEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(PriestEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		PriestEntity entity = (PriestEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "priest")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	private static final class AnimatedModel extends Modelpriest {
		private final KeyframeAnimation keyframeAnimation0;
		private final KeyframeAnimation keyframeAnimation1;

		public AnimatedModel(ModelPart root) {
			super(root);
			this.keyframeAnimation0 = safeBake(priestAnimation.spawn);
			this.keyframeAnimation1 = safeBake(priestAnimation.idle);
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
			PriestEntity entity = state.getRenderData(ENTITY_KEY);
			if (entity != null) {
				this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);
				this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);
			}
			super.setupAnim(state);
		}
	}

	public static final ContextKey<PriestEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:priest_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(PriestRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}