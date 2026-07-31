package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.mcreator.minigames.client.model.Modeldemon;
import net.mcreator.minigames.client.model.animations.demonAnimation;
import net.mcreator.minigames.entity.DemonEntity;

import java.util.Map;

public class DemonRenderer extends MobRenderer<DemonEntity, DemonRenderer.DemonRenderState, DemonRenderer.AnimatedModel> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.parse("minigames:textures/entities/demon.png");

    public DemonRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedModel(context.bakeLayer(Modeldemon.LAYER_LOCATION)), 1.0f);

        // Emissive eyes layer
        this.addLayer(new RenderLayer<>(this) {
            private static final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/demon_emissive.png");
            private static final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);

            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, DemonRenderState state, float headYaw, float headPitch) {
                submitNodeCollector.submitModel(this.getParentModel(), state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
            }
        });
    }

    // Custom RenderState to carry animation data to the rendering thread
    public static class DemonRenderState extends LivingEntityRenderState {
        public final AnimationState animationState0 = new AnimationState();
        public final AnimationState animationState1 = new AnimationState();
        public Identifier texture = DEFAULT_TEXTURE;
    }

    @Override
    public DemonRenderState createRenderState() {
        return new DemonRenderState();
    }

    @Override
    public void extractRenderState(DemonEntity entity, DemonRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        // Safely sync animation states
        state.animationState0.copyFrom(entity.animationState0);
        state.animationState1.copyFrom(entity.animationState1);

        // Resolve entity texture
        if (entity.getTexture() != null && !entity.getTexture().equals("demon")) {
            state.texture = Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
        } else {
            state.texture = DEFAULT_TEXTURE;
        }
    }

    @Override
    public Identifier getTextureLocation(DemonRenderState state) {
        return state.texture;
    }

    @Override
    protected void scale(DemonRenderState state, PoseStack poseStack) {
        poseStack.scale(1.5f, 1.5f, 1.5f);
    }

    public static final class AnimatedModel extends Modeldemon {
        private final KeyframeAnimation keyframeAnimation0;
        private final KeyframeAnimation keyframeAnimation1;

        public AnimatedModel(ModelPart root) {
            super(root);
            this.keyframeAnimation0 = safeBake(demonAnimation.ability);
            this.keyframeAnimation1 = safeBake(demonAnimation.reload);
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
            super.setupAnim(state);
            this.root().getAllParts().forEach(ModelPart::resetPose);

            if (state instanceof DemonRenderState customState) {
                this.keyframeAnimation0.apply(customState.animationState0, state.ageInTicks, 1.0f);
                this.keyframeAnimation1.apply(customState.animationState1, state.ageInTicks, 1.0f);
            }
        }
    }
}