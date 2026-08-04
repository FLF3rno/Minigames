package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.minigames.ModDataAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class ProcedureBeamRenderManager {

    public static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/misc/white.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

        // Scans all entities in range carrying BEAM_DATA (mobs, players, projectiles, etc.)
        renderProcedureBeams(bufferSource, cameraPos, mc.level.getEntities((Entity) null,
                new AABB(cameraPos, cameraPos).inflate(256.0D),
                entity -> entity.hasData(ModDataAttachments.BEAM_DATA)), partialTick);
    }

    private static void renderProcedureBeams(MultiBufferSource bufferSource, Vec3 cameraPos, Iterable<? extends Entity> entities, float partialTick) {
        for (Entity source : entities) {
            try {
                ModDataAttachments.BeamData data = source.getData(ModDataAttachments.BEAM_DATA);
                if (data != null && data.hasBeam && (source.tickCount - data.startTick) <= data.durationTicks) {
                    Entity target = source.level().getEntity(data.targetId);
                    if (target == null || !target.isAlive()) {
                        continue;
                    }

                    Vec3 start = source.getEyePosition(partialTick);
                    Vec3 end = target.getEyePosition(partialTick);

                    renderBeamQuad(bufferSource, cameraPos, start, end, data.texture, source.tickCount + partialTick, data.scale);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void renderBeamQuad(MultiBufferSource bufferSource, Vec3 cameraPos, Vec3 startWorld, Vec3 endWorld,
                                       String texturePath, float tickTime, double scale) {
        Identifier texture = (texturePath == null || texturePath.isEmpty()) ? DEFAULT_TEXTURE : Identifier.parse(texturePath);
        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.entityTranslucent(texture));

        Vec3 start = startWorld.subtract(cameraPos);
        Vec3 end = endWorld.subtract(cameraPos);
        Vec3 dir = end.subtract(start);

        if (dir.lengthSqr() < 1.0E-6D) return;

        Vec3 normDir = dir.normalize();

        // Billboard orientation toward camera
        Vec3 camDir = start.scale(-1).normalize();
        Vec3 side = normDir.cross(camDir);

        if (side.lengthSqr() < 1.0E-6D) {
            side = normDir.cross(new Vec3(0.0D, 1.0D, 0.0D));
        }
        if (side.lengthSqr() < 1.0E-6D) {
            side = normDir.cross(new Vec3(1.0D, 0.0D, 0.0D));
        }

        side = side.normalize();
        Vec3 widthVec = side.scale(Math.max(0.001D, scale) * 0.12D);

        double length = start.distanceTo(end);
        int segments = Math.max(1, Mth.ceil(length / 0.5D));
        float animationOffset = (tickTime * 0.1F) % 1.0F;

        for (int i = 0; i < segments; i++) {
            float u0 = (float) i / segments - animationOffset;
            float u1 = (float) (i + 1) / segments - animationOffset;

            Vec3 segStart = start.add(normDir.scale(length * ((double) i / segments)));
            Vec3 segEnd = start.add(normDir.scale(length * ((double) (i + 1) / segments)));

            drawQuad(buffer, segStart, segEnd, widthVec, u0, u1, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void drawQuad(VertexConsumer buffer, Vec3 start, Vec3 end, Vec3 width, float u0, float u1, float r, float g, float b, float alpha) {
        Vec3 sR = start.add(width);
        Vec3 sL = start.subtract(width);
        Vec3 eR = end.add(width);
        Vec3 eL = end.subtract(width);

        buffer.addVertex((float) sR.x, (float) sR.y, (float) sR.z).setColor(r, g, b, alpha).setUv(u0, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex((float) eR.x, (float) eR.y, (float) eR.z).setColor(r, g, b, alpha).setUv(u1, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex((float) eL.x, (float) eL.y, (float) eL.z).setColor(r, g, b, alpha).setUv(u1, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
        buffer.addVertex((float) sL.x, (float) sL.y, (float) sL.z).setColor(r, g, b, alpha).setUv(u0, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(0, 1, 0);
    }
}