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
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.lightning());
        boolean renderedAny = false;

        for (Player player : mc.level.players()) {
            try {
                if (!shouldRenderAscensionBeam(player)) {
                    continue;
                }

                Vec3 origin = getGroundOrigin(player);
                renderBeam(buffer, origin, cameraPos);
                renderedAny = true;
            } catch (Exception ignored) {
            }
        }

        if (renderedAny) {
            bufferSource.endBatch(RenderTypes.lightning());
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
            VertexConsumer buffer,
            Vec3 origin,
            Vec3 cameraPos
    ) {
        float ox = (float) (origin.x - cameraPos.x);
        float oy = (float) (origin.y - cameraPos.y);
        float oz = (float) (origin.z - cameraPos.z);

        float r = 1.0F;
        float g = 0.78F;
        float b = 0.20F;
        float alpha = 0.75F;

        addFace(
                buffer,
                ox, oy, oz,
                -BASE_RADIUS, -BASE_RADIUS,
                -TOP_RADIUS, -TOP_RADIUS,
                BASE_RADIUS, -BASE_RADIUS,
                TOP_RADIUS, -TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                buffer,
                ox, oy, oz,
                BASE_RADIUS, BASE_RADIUS,
                TOP_RADIUS, TOP_RADIUS,
                -BASE_RADIUS, BASE_RADIUS,
                -TOP_RADIUS, TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                buffer,
                ox, oy, oz,
                BASE_RADIUS, -BASE_RADIUS,
                TOP_RADIUS, -TOP_RADIUS,
                BASE_RADIUS, BASE_RADIUS,
                TOP_RADIUS, TOP_RADIUS,
                r, g, b, alpha
        );

        addFace(
                buffer,
                ox, oy, oz,
                -BASE_RADIUS, BASE_RADIUS,
                -TOP_RADIUS, TOP_RADIUS,
                -BASE_RADIUS, -BASE_RADIUS,
                -TOP_RADIUS, -TOP_RADIUS,
                r, g, b, alpha
        );
    }

    private static void addFace(
            VertexConsumer buffer,
            float ox, float oy, float oz,
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
        buffer.addVertex(ox + x1Bottom, oy + 0.0F, oz + z1Bottom)
                .setColor(r, g, b, alpha)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(ox + x1Top, oy + BEAM_HEIGHT, oz + z1Top)
                .setColor(r, g, b, alpha)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(ox + x2Top, oy + BEAM_HEIGHT, oz + z2Top)
                .setColor(r, g, b, alpha)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);

        buffer.addVertex(ox + x2Bottom, oy + 0.0F, oz + z2Bottom)
                .setColor(r, g, b, alpha)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(0.0F, 1.0F, 0.0F);
    }
}