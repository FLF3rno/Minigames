package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class FreeBeamRenderManager {

    private static final float BEAM_HEIGHT = 20.0F;
    private static final float BASE_RADIUS = 1.5F;
    private static final float TOP_RADIUS = 0.25F;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();

        for (Player player : mc.level.players()) {
            try {
                if (!shouldRenderAscensionBeam(player)) {
                    continue;
                }

                Vec3 origin = getGroundOrigin(player);

                poseStack.pushPose();
                poseStack.translate(
                        origin.x - cameraPos.x,
                        origin.y - cameraPos.y,
                        origin.z - cameraPos.z
                );

                renderBeam(poseStack, bufferSource);

                poseStack.popPose();
            } catch (Exception ignored) {
            }
        }
    }

    private static boolean shouldRenderAscensionBeam(Player player) {
        MobEffectInstance effect = player.getEffect(MinigamesModMobEffects.ASCENDING);
        MinigamesModVariables.PlayerVariables vars =
                player.getData(MinigamesModVariables.PLAYER_VARIABLES);

        return effect != null
                || (vars != null && (vars.ascendingActive || vars.ascendingTimer > 0));
    }

    private static Vec3 getGroundOrigin(Player player) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(
                (int) Math.floor(player.getX()),
                (int) Math.floor(player.getY()),
                (int) Math.floor(player.getZ())
        );

        while (
                pos.getY() > player.level().getMinY()
                        && player.level().getBlockState(pos).isAir()
        ) {
            pos.move(0, -1, 0);
        }

        return new Vec3(
                player.getX(),
                pos.getY() + 1.0D,
                player.getZ()
        );
    }

    private static void renderBeam(
            PoseStack poseStack,
            MultiBufferSource bufferSource
    ) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lightning());
        Matrix4f matrix = poseStack.last().pose();

        float r = 1.0F;
        float g = 0.78F;
        float b = 0.20F;
        float alpha = 0.75F;

        addFace(
                matrix,
                buffer,
                -BASE_RADIUS, -BASE_RADIUS,
                -TOP_RADIUS, -TOP_RADIUS,
                BASE_RADIUS, -BASE_RADIUS,
                TOP_RADIUS, -TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                matrix,
                buffer,
                BASE_RADIUS, BASE_RADIUS,
                TOP_RADIUS, TOP_RADIUS,
                -BASE_RADIUS, BASE_RADIUS,
                -TOP_RADIUS, TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                matrix,
                buffer,
                BASE_RADIUS, -BASE_RADIUS,
                TOP_RADIUS, -TOP_RADIUS,
                BASE_RADIUS, BASE_RADIUS,
                TOP_RADIUS, TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                matrix,
                buffer,
                -BASE_RADIUS, BASE_RADIUS,
                -TOP_RADIUS, TOP_RADIUS,
                -BASE_RADIUS, -BASE_RADIUS,
                -TOP_RADIUS, -TOP_RADIUS,
                r, g, b, alpha
        );
    }

    private static void addFace(
            Matrix4f matrix,
            VertexConsumer buffer,
            float x1Bottom,
            float z1Bottom,
            float x1Top,
            float z1Top,
            float x2Bottom,
            float z2Bottom,
            float x2Top,
            float z2Top,
            float r,
            float g,
            float b,
            float alpha
    ) {
        buffer.addVertex(matrix, x1Bottom, 0.0F, z1Bottom)
                .setColor(r, g, b, alpha)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(matrix, x1Top, BEAM_HEIGHT, z1Top)
                .setColor(r, g, b, alpha)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(matrix, x2Top, BEAM_HEIGHT, z2Top)
                .setColor(r, g, b, alpha)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(matrix, x2Bottom, 0.0F, z2Bottom)
                .setColor(r, g, b, alpha)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);
    }
}