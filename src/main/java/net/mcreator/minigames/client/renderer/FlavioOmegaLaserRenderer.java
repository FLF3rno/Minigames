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
import net.minecraft.util.Mth;
import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.client.model.animations.flavio_omega_laserAnimation;
import net.mcreator.minigames.client.model.Modelflavio_omega_laser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class FlavioOmegaLaserRenderer extends MobRenderer<FlavioOmegaLaserEntity, LivingEntityRenderState, Modelflavio_omega_laser> {

    private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/omega_laser.png");



    public FlavioOmegaLaserRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedModel(context.bakeLayer(Modelflavio_omega_laser.LAYER_LOCATION)), 0.5f);

        this.addLayer(new RenderLayer<>(this) {
            private static final Identifier LAYER_TEXTURE =
                    Identifier.parse("minigames:textures/entities/omega_laser.png");

            private static final RenderType RENDER_TYPE =
                    RenderTypes.eyes(LAYER_TEXTURE);

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
    public void extractRenderState(FlavioOmegaLaserEntity entity, LivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override

    public Identifier getTextureLocation(LivingEntityRenderState state) {

        FlavioOmegaLaserEntity entity = (FlavioOmegaLaserEntity) state.getRenderData(ENTITY_KEY);

        if (entity != null && entity.getTexture() != "omega_laser")

            return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");

        return entityTexture;

    }

    private static final class AnimatedModel extends Modelflavio_omega_laser {

        private final KeyframeAnimation keyframeAnimation0;
        private float smoothedYaw;
        private float smoothedPitch;
        public AnimatedModel(ModelPart root) {
            super(root);
            this.keyframeAnimation0 = safeBake(flavio_omega_laserAnimation.fire);
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

            FlavioOmegaLaserEntity entity = state.getRenderData(FlavioOmegaLaserRenderer.ENTITY_KEY);

            if (entity != null) {
                this.keyframeAnimation0.apply(entity.animationState0, state.ageInTicks, 1.0F);
                Player player = entity.level().getNearestPlayer(entity, 60.0D);
                if (player != null) {
                    Vec3 eyes = entity.getEyePosition();
                    Vec3 target = player.getEyePosition();
                    double dx = target.x - eyes.x;
                    double dy = target.y - eyes.y;
                    double dz = target.z - eyes.z;
                    double horizontal = Math.sqrt(dx * dx + dz * dz);
                    float targetYaw = (float) Math.toDegrees(Math.atan2(dx, dz)) * -1.0F - 11;
                    float targetPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontal)) + 20.0F;

                    this.smoothedYaw = Mth.approachDegrees(this.smoothedYaw, targetYaw, 2.0F);
                    this.smoothedPitch = Mth.approachDegrees(this.smoothedPitch, targetPitch, 2.0F);

                    this.head.yRot = this.smoothedYaw * ((float) Math.PI / 180F);
                    this.head.xRot = this.smoothedPitch * ((float) Math.PI / 180F);
                }
            }
        }

    }

    public static final ContextKey<FlavioOmegaLaserEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_omega_laser_entity"));

    @EventBusSubscriber(Dist.CLIENT)

    public static class EntityStateAdder {

        @SubscribeEvent

        private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {

            event.registerEntityModifier(FlavioOmegaLaserRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));

        }

    }

}
