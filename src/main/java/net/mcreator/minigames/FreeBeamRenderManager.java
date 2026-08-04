package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class FreeBeamRenderManager {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        renderBeams(poseStack, bufferSource, cameraPos, mc.level.players());
    }

    public static void renderBeams(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, Iterable<? extends Player> players) {
        for (Player player : players) {
            try {
                if (player.hasData(ModDataAttachments.BEAM_DATA)) {
                    ModDataAttachments.BeamData data = player.getData(ModDataAttachments.BEAM_DATA);

                    if (data.hasBeam) {
                        // Position relative to camera view using data coordinates
                        double renderX = data.x - cameraPos.x;
                        double renderY = data.y - cameraPos.y;
                        double renderZ = data.z - cameraPos.z;

                        poseStack.pushPose();
                        poseStack.translate(renderX, renderY, renderZ);

                        renderHolyYellowBeam(poseStack, bufferSource);

                        poseStack.popPose();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void renderHolyYellowBeam(PoseStack poseStack, MultiBufferSource bufferSource) {
        float height = 50.0f;

        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.debugQuads());
        Matrix4f matrix = poseStack.last().pose();

        // Single 3D Tapered Box using translucent color fill
        float r = 1.0f;
        float g = 0.90f;
        float b = 0.20f;
        float alpha = 0.45f;

        drawTaperedBox(matrix, buffer, 2.5f, 1.0f, height, r, g, b, alpha);
    }

    private static void drawTaperedBox(Matrix4f matrix, VertexConsumer buffer, float bRad, float tRad, float height, float r, float g, float b, float alpha) {
        addDoubleSidedQuad(matrix, buffer, -bRad, -bRad, -tRad, -tRad,  bRad, -bRad,  tRad, -tRad, height, r, g, b, alpha);
        addDoubleSidedQuad(matrix, buffer,  bRad,  bRad,  tRad,  tRad, -bRad,  bRad, -tRad,  tRad, height, r, g, b, alpha);
        addDoubleSidedQuad(matrix, buffer,  bRad, -bRad,  tRad, -tRad,  bRad,  bRad,  tRad,  tRad, height, r, g, b, alpha);
        addDoubleSidedQuad(matrix, buffer, -bRad,  bRad, -tRad,  tRad, -bRad, -bRad, -tRad, -tRad, height, r, g, b, alpha);
    }

    private static void addDoubleSidedQuad(Matrix4f matrix, VertexConsumer buffer,
                                           float x1Bottom, float z1Bottom, float x1Top, float z1Top,
                                           float x2Bottom, float z2Bottom, float x2Top, float z2Top,
                                           float yTop, float r, float g, float b, float alpha) {
        float yBottom = 0.0f;

        // Front Face
        buffer.addVertex(matrix, x1Bottom, yBottom, z1Bottom).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x1Top, yTop, z1Top).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x2Top, yTop, z2Top).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x2Bottom, yBottom, z2Bottom).setColor(r, g, b, alpha);

        // Back Face
        buffer.addVertex(matrix, x2Bottom, yBottom, z2Bottom).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x2Top, yTop, z2Top).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x1Top, yTop, z1Top).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, x1Bottom, yBottom, z1Bottom).setColor(r, g, b, alpha);
    }
}