package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterParticles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        for (Player player : mc.level.players()) {
            try {
                if (player.hasData(ModDataAttachments.BEAM_DATA)) {
                    ModDataAttachments.BeamData data = player.getData(ModDataAttachments.BEAM_DATA);
                    
                    if (data.hasBeam) {
                        poseStack.pushPose();

                        double renderX = data.x - cameraPos.x;
                        double renderY = data.y - cameraPos.y;
                        double renderZ = data.z - cameraPos.z;
                        poseStack.translate(renderX, renderY, renderZ);

                        renderSolidTaperingBox(poseStack, bufferSource);

                        poseStack.popPose();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void renderSolidTaperingBox(PoseStack poseStack, MultiBufferSource bufferSource) {
        float height = 20.0f;
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
}