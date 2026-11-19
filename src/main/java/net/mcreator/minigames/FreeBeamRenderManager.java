package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class FreeBeamRenderManager {

    private static final float BEAM_HEIGHT = 20.0F;
    private static final float BASE_RADIUS = 1.5F;
    private static final float TOP_RADIUS = 0.25F;

    @SubscribeEvent
    public static void onRenderLevelStage(SubmitCustomGeometryEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = mc.gameRenderer.mainCamera().position();
        java.util.List<Vec3> origins = new java.util.ArrayList<>();

        for (Player player : mc.level.players()) {
            try {
                if (!shouldRenderAscensionBeam(player)) {
                    continue;
                }

                origins.add(getGroundOrigin(player));
            } catch (Exception ignored) {
            }
        }

        if (!origins.isEmpty()) {
            event.getSubmitNodeCollector().submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, buffer) -> {
                for (Vec3 origin : origins) {
                    renderBeam(buffer, origin, cameraPos);
                }
            });
        }
    }

    private static boolean shouldRenderAscensionBeam(Player player) {
        return player.hasEffect(MinigamesModMobEffects.ASCENDING);
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