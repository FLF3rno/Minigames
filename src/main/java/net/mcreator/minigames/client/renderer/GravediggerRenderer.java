package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.minigames.entity.GravediggerEntity;
import net.mcreator.minigames.client.model.animations.gravediggerAnimation;
import net.mcreator.minigames.client.model.animations.candleheadAnimation;
import net.mcreator.minigames.client.model.Modelgravedigger;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class GravediggerRenderer extends MobRenderer<GravediggerEntity, LivingEntityRenderState, Modelgravedigger> {
    private GravediggerEntity entity = null;
    private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/gravedigger.png");

    public GravediggerRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedModel(context.bakeLayer(Modelgravedigger.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<>(this) {
            final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("minigames:textures/entities/gravedigger_emissive.png");

            @Override
            public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, LivingEntityRenderState state, float limbSwing, float limbSwingAmount) {
                EntityModel<LivingEntityRenderState> model = this.getParentModel();
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
                model.renderToBuffer(poseStack, vertexConsumer, 15728880, LivingEntityRenderer.getOverlayCoords(state, 0));
            }
        });
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public void extractRenderState(GravediggerEntity entity, LivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        this.entity = entity;
        if (this.model instanceof AnimatedModel) {
            ((AnimatedModel) this.model).setEntity(entity);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
        if (entity != null && !"gravedigger".equals(entity.getTexture()))
            return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
        return entityTexture;
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(0.9f, 0.9f, 0.9f);
    }

    private static final class AnimatedModel extends Modelgravedigger {
        private GravediggerEntity entity = null;
        private final KeyframeAnimation keyframeAnimation0;
        private final KeyframeAnimation keyframeAnimation1;
        private final KeyframeAnimation keyframeAnimation2;

        public AnimatedModel(ModelPart root) {
            super(root);
            this.keyframeAnimation0 = safeBake(gravediggerAnimation.walk);
            this.keyframeAnimation1 = safeBake(candleheadAnimation.attack);
            this.keyframeAnimation2 = safeBake(gravediggerAnimation.dig);
        }

        private KeyframeAnimation safeBake(AnimationDefinition source) {
            try {
                return source.bake(root);
            } catch (IllegalArgumentException e) {
                return new AnimationDefinition(0, false, Map.of()).bake(root);
            }
        }

        public void setEntity(GravediggerEntity entity) {
            this.entity = entity;
        }

        @Override
        public void setupAnim(LivingEntityRenderState state) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            this.keyframeAnimation0.apply(entity.animationState1, state.ageInTicks, 1f);
            this.keyframeAnimation1.apply(entity.animationState3, state.ageInTicks, 1f);
            this.keyframeAnimation2.apply(entity.animationState2, state.ageInTicks, 1f);
            super.setupAnim(state);
        }
    }
}
