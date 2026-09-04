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
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.AnimationWalkingProcedure;
import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.client.model.animations.flavioAnimation;
import net.mcreator.minigames.client.model.Modelflavio;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class FlavioRenderer extends MobRenderer<FlavioEntity, LivingEntityRenderState, FlavioRenderer.AnimatedModel> {
    private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/flavio.png");

    public FlavioRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedModel(context.bakeLayer(Modelflavio.LAYER_LOCATION)), 0.5f);

        this.addLayer(new RenderLayer<>(this) {
            final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/flavioemissive.png");
            final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);
            final AnimatedModel LAYER_MODEL = new AnimatedModel(Minecraft.getInstance().getEntityModels().bakeLayer(Modelflavio.LAYER_LOCATION));

            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
                LAYER_MODEL.setupAnim(state);

                submitNodeCollector.submitModel(LAYER_MODEL, state, poseStack, RENDER_TYPE, 0xF000F0, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
            }
        });
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public void extractRenderState(
            FlavioEntity entity,
            LivingEntityRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        FlavioEntity entity = state.getRenderData(ENTITY_KEY);

        if (entity != null && entity.getTexture() != "flavio")
            return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");

        return entityTexture;
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(0.75f, 0.75f, 0.75f);
    }

    public static final class AnimatedModel extends Modelflavio {
        private final KeyframeAnimation keyframeAnimation0;
        private final KeyframeAnimation keyframeAnimation1;
        private final KeyframeAnimation keyframeAnimation2;
        private final KeyframeAnimation keyframeAnimation3;
        private final KeyframeAnimation keyframeAnimation4;

        public AnimatedModel(ModelPart root) {
            super(root);

            this.keyframeAnimation0 = safeBake(flavioAnimation.idle);
            this.keyframeAnimation1 = safeBake(flavioAnimation.press_button);
            this.keyframeAnimation2 = safeBake(flavioAnimation.run);
            this.keyframeAnimation3 = safeBake(flavioAnimation.punch);
            this.keyframeAnimation4 = safeBake(flavioAnimation.death);
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

            super.setupAnim(state);

            FlavioEntity entity = state.getRenderData(ENTITY_KEY);

            if (entity == null)
                return;

            this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1f);

            this.keyframeAnimation1.apply(entity.animationState1, state.ageInTicks, 1f);

            if (AnimationWalkingProcedure.execute(entity)) {
                this.keyframeAnimation2.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1f, 1f);
            }

            this.keyframeAnimation3.apply(entity.animationState3, state.ageInTicks, 1f);

            this.keyframeAnimation4.apply(entity.animationState4, state.ageInTicks, 1f);
        }
    }

    public static final ContextKey<FlavioEntity> ENTITY_KEY =
            new ContextKey<>(Identifier.parse("minigames:flavio_entity"));

    @EventBusSubscriber(Dist.CLIENT)
    public static class EntityStateAdder {

        @SubscribeEvent
        private static void registerRenderStateModifiersEvent(
                RegisterRenderStateModifiersEvent event
        ) {
            event.registerEntityModifier(FlavioRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
        }
    }
}