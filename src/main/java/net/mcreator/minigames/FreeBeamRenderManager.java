package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class FreeBeamRenderManager {

    public static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/misc/white.png");
    private static final float BEAM_HEIGHT = 20.0f;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterParticles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        renderBeams(poseStack, bufferSource, cameraPos, mc.level.players(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }

    public static void renderBeams(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, Iterable<? extends Player> players, float partialTick) {
        for (Player player : players) {
            try {
                if (player.hasData(ModDataAttachments.BEAM_DATA)) {
                    ModDataAttachments.BeamData data = player.getData(ModDataAttachments.BEAM_DATA);

                    if (data.hasBeam) {
                        poseStack.pushPose();

                        double renderX = data.x - cameraPos.x;
                        double renderY = data.y - cameraPos.y;
                        double renderZ = data.z - cameraPos.z;
                        poseStack.translate(renderX, renderY, renderZ);

                        float tickTime = player.tickCount + partialTick;
                        renderSolidTaperingBox(poseStack, bufferSource);
                        renderCloudCap(poseStack, bufferSource, tickTime);

                        poseStack.popPose();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void renderSolidTaperingBox(PoseStack poseStack, MultiBufferSource bufferSource) {
        float height = BEAM_HEIGHT;
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.beaconBeam(WHITE_TEXTURE, true));
        Matrix4f matrix = poseStack.last().pose();

        float r = 1.0f;
        float g = 0.88f;
        float b = 0.35f;
        float alpha = 0.50f;

        float bRad = 1.5f; 
        float tRad = 0.25f;

        addConeQuad(matrix, buffer, -bRad, -bRad, -tRad, -tRad,  bRad, -bRad,  tRad, -tRad, 0, height, r, g, b, alpha);
        addConeQuad(matrix, buffer,  bRad,  bRad,  tRad,  tRad, -bRad,  bRad, -tRad,  tRad, 0, height, r, g, b, alpha);
        addConeQuad(matrix, buffer,  bRad, -bRad,  tRad, -tRad,  bRad,  bRad,  tRad,  tRad, 0, height, r, g, b, alpha);
        addConeQuad(matrix, buffer, -bRad,  bRad, -tRad,  tRad, -bRad, -bRad, -tRad, -tRad, 0, height, r, g, b, alpha);
    }

    private static void addConeQuad(Matrix4f matrix, VertexConsumer buffer, 
                                    float x1Bottom, float z1Bottom, float x1Top, float z1Top,
                                    float x2Bottom, float z2Bottom, float x2Top, float z2Top,
                                    float yBottom, float yTop, float r, float g, float b, float alpha) {
        
        buffer.addVertex(matrix, x1Bottom, yBottom, z1Bottom).setColor(r, g, b, alpha).setUv(0.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1Top, yTop, z1Top).setColor(r, g, b, alpha).setUv(0.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2Top, yTop, z2Top).setColor(r, g, b, alpha).setUv(1.0f, 0.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2Bottom, yBottom, z2Bottom).setColor(r, g, b, alpha).setUv(1.0f, 1.0f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
    }

    private static void renderCloudCap(PoseStack poseStack, MultiBufferSource bufferSource, float tickTime) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugQuads());
        Matrix4f matrix = poseStack.last().pose();

        float centerY = BEAM_HEIGHT + 0.35f + Mth.sin(tickTime * 0.06f) * 0.12f;
        float baseAlpha = 0.32f + Mth.sin(tickTime * 0.08f) * 0.06f;

        float r = 0.98f;
        float g = 0.98f;
        float b = 1.0f;

        // Build mostly-horizontal cloud lobes so the cap reads as fluffy mass, not vertical card spam.
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.06f, 0.0f, 0.0f, 2.15f, r, g, b, baseAlpha * 0.82f);
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.22f, 0.52f, -0.34f, 1.55f, r, g, b, baseAlpha * 0.78f);
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.18f, -0.6f, 0.3f, 1.48f, r, g, b, baseAlpha * 0.76f);
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.34f, 0.22f, 0.62f, 1.28f, r, g, b, baseAlpha * 0.72f);
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.3f, -0.3f, -0.64f, 1.2f, r, g, b, baseAlpha * 0.70f);
        addHorizontalPuffQuad(matrix, buffer, centerY + 0.52f, 0.0f, 0.0f, 1.04f, r, g, b, baseAlpha * 0.66f);

    }

    private static void addPuffLayer(Matrix4f matrix, VertexConsumer buffer, float y, float centerX, float centerZ, float radius, float thickness, float rotationDeg, float r, float g, float b, float alpha) {
        float rot = (float) Math.toRadians(rotationDeg);
        float cs = Mth.cos(rot);
        float sn = Mth.sin(rot);

        addVerticalPuffQuad(matrix, buffer, y, centerX, centerZ, radius, thickness, cs, sn, r, g, b, alpha);
        addVerticalPuffQuad(matrix, buffer, y, centerX, centerZ, radius, thickness, -sn, cs, r, g, b, alpha);
        addHorizontalPuffQuad(matrix, buffer, y, centerX, centerZ, radius * 0.95f, r, g, b, alpha * 0.82f);
    }

    private static void addVerticalPuffQuad(Matrix4f matrix, VertexConsumer buffer, float y, float centerX, float centerZ, float halfWidth, float halfHeight, float dirX, float dirZ, float r, float g, float b, float alpha) {
        float x1 = centerX - dirX * halfWidth;
        float z1 = centerZ - dirZ * halfWidth;
        float x2 = centerX + dirX * halfWidth;
        float z2 = centerZ + dirZ * halfWidth;

        buffer.addVertex(matrix, x1, y - halfHeight, z1).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y + halfHeight, z1).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y + halfHeight, z2).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y - halfHeight, z2).setColor(r, g, b, alpha).setNormal(0, 1, 0);
    }

    private static void addHorizontalPuffQuad(Matrix4f matrix, VertexConsumer buffer, float y, float centerX, float centerZ, float radius, float r, float g, float b, float alpha) {
        buffer.addVertex(matrix, centerX - radius, y, centerZ - radius).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, centerX - radius, y, centerZ + radius).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, centerX + radius, y, centerZ + radius).setColor(r, g, b, alpha).setNormal(0, 1, 0);
        buffer.addVertex(matrix, centerX + radius, y, centerZ - radius).setColor(r, g, b, alpha).setNormal(0, 1, 0);
    }
}
