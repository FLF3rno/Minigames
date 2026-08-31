package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
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
public class ProcedureBeamXYZRenderManager {

    private static final Identifier DEFAULT_TEXTURE =
            Identifier.fromNamespaceAndPath("minecraft", "textures/misc/white.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null
                || mc.player == null
                || mc.gameRenderer == null
                || mc.gameRenderer.getMainCamera() == null) {
            return;
        }

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        for (Entity source : mc.level.getEntities(
                (Entity) null,
                new AABB(cameraPos, cameraPos).inflate(512.0D),
                entity -> entity.hasData(ModDataAttachments.BEAM_XYZ_DATA))) {

            try {
                ModDataAttachments.BeamXYZData data =
                        source.getData(ModDataAttachments.BEAM_XYZ_DATA);

                if (data == null || !data.active) {
                    continue;
                }

                if (!source.isAlive() || (source.tickCount - data.startTick) > data.durationTicks) {
                    clearBeamData(source);
                    continue;
                }

                Vec3 startWorld = new Vec3(data.fromX, data.fromY, data.fromZ);
                Vec3 endWorld = new Vec3(data.toX, data.toY, data.toZ);

                Vec3 start = startWorld.subtract(cameraPos);
                Vec3 end = endWorld.subtract(cameraPos);

                float tickTime = source.tickCount + partialTick;

                if ("sprite".equalsIgnoreCase(data.type) || "billboard".equalsIgnoreCase(data.type)) {
                    renderSprite(bufferSource, start, end, data.scale, data.texture, data.emissive, tickTime);
                } else {
                    renderBeam(bufferSource, start, end, data.scale, data.texture, data.emissive, tickTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bufferSource.endBatch();
    }

    private static Identifier getTexture(String path) {
        if (path == null || path.isEmpty()) return DEFAULT_TEXTURE;
        try {
            return Identifier.parse(path);
        } catch (Exception e) {
            return DEFAULT_TEXTURE;
        }
    }

    private static void renderBeam(
            MultiBufferSource.BufferSource buffers,
            Vec3 start,
            Vec3 end,
            double scale,
            String texturePath,
            boolean emissive,
            float tickTime
    ) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        if (length < 0.001D) return;

        Vec3 forward = direction.normalize();
        Vec3 reference = Math.abs(forward.y) < 0.95D ? new Vec3(0.0D, 1.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
        Vec3 side = reference.cross(forward);
        if (side.lengthSqr() < 1.0E-6D) {
            side = new Vec3(0.0D, 0.0D, 1.0D).cross(forward);
        }
        side = side.normalize().scale(Math.max(0.001D, scale) * 0.12D);

        Vec3 up = forward.cross(side).normalize().scale(Math.max(0.001D, scale) * 0.12D);

        RenderType renderType = RenderTypes.entityTranslucent(getTexture(texturePath));
        VertexConsumer buffer = buffers.getBuffer(renderType);

        int segments = Math.max(1, Mth.ceil(length / 0.5D));
        float animationOffset = (tickTime * 0.1F) % 1.0F;

        for (int i = 0; i < segments; i++) {
            double startPercent = (double) i / segments;
            double endPercent = (double) (i + 1) / segments;

            Vec3 segStart = start.add(forward.scale(length * startPercent));
            Vec3 segEnd = start.add(forward.scale(length * endPercent));

            float u0 = (float) startPercent - animationOffset;
            float u1 = (float) endPercent - animationOffset;

            drawPrism(buffer, segStart, segEnd, side, up, u0, u1, emissive);
        }
    }

    private static void renderSprite(
            MultiBufferSource.BufferSource buffers,
            Vec3 start,
            Vec3 end,
            double scale,
            String texturePath,
            boolean emissive,
            float tickTime
    ) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        if (length < 0.001D) return;

        Vec3 forward = direction.normalize();
        Vec3 reference = Math.abs(forward.y) < 0.95D ? new Vec3(0.0D, 1.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
        Vec3 side = reference.cross(forward);
        if (side.lengthSqr() < 1.0E-6D) {
            side = new Vec3(0.0D, 0.0D, 1.0D).cross(forward);
        }
        side = side.normalize().scale(Math.max(0.001D, scale) * 0.12D);

        Vec3 up = forward.cross(side).normalize().scale(Math.max(0.001D, scale) * 0.12D);

        RenderType renderType = RenderTypes.entityTranslucent(getTexture(texturePath));
        VertexConsumer buffer = buffers.getBuffer(renderType);

        float offset = (tickTime * 0.1F) % 1.0F;

        addQuad(buffer, start.add(side), end.add(side), end.subtract(side), start.subtract(side), -offset, (float) length - offset, emissive);
        addQuad(buffer, start.subtract(side), end.subtract(side), end.add(side), start.add(side), -offset, (float) length - offset, emissive);
        addQuad(buffer, start.add(up), end.add(up), end.subtract(up), start.subtract(up), -offset, (float) length - offset, emissive);
        addQuad(buffer, start.subtract(up), end.subtract(up), end.add(up), start.add(up), -offset, (float) length - offset, emissive);
    }

    private static void drawPrism(
            VertexConsumer buffer,
            Vec3 start,
            Vec3 end,
            Vec3 side,
            Vec3 up,
            float u0,
            float u1,
            boolean emissive
    ) {
        Vec3 sR = start.add(side);
        Vec3 sL = start.subtract(side);

        Vec3 sU = start.add(up);
        Vec3 sD = start.subtract(up);

        Vec3 eR = end.add(side);
        Vec3 eL = end.subtract(side);

        Vec3 eU = end.add(up);
        Vec3 eD = end.subtract(up);

        addQuad(buffer, sR, eR, eL, sL, u0, u1, emissive);
        addQuad(buffer, sD, eD, eU, sU, u0, u1, emissive);
        addQuad(buffer, sU, eU, eR, sR, u0, u1, emissive);
        addQuad(buffer, sL, eL, eD, sD, u0, u1, emissive);
    }

    private static void addQuad(
            VertexConsumer buffer,
            Vec3 a,
            Vec3 b,
            Vec3 c,
            Vec3 d,
            float u0,
            float u1,
            boolean emissive
    ) {
        int light = emissive ? 15728880 : 15728880;

        buffer.addVertex((float) a.x, (float) a.y, (float) a.z)
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u0, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        buffer.addVertex((float) b.x, (float) b.y, (float) b.z)
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u1, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        buffer.addVertex((float) c.x, (float) c.y, (float) c.z)
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u1, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        buffer.addVertex((float) d.x, (float) d.y, (float) d.z)
                .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                .setUv(u0, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);
    }

    private static void clearBeamData(Entity source) {
        try {
            source.setData(
                    ModDataAttachments.BEAM_XYZ_DATA,
                    new ModDataAttachments.BeamXYZData(
                            false,
                            0.0D,
                            0.0D,
                            0.0D,
                            0.0D,
                            0.0D,
                            0.0D,
                            0,
                            0,
                            0.0D,
                            "",
                            "beam",
                            false
                    )
            );

            if (!source.level().isClientSide()) {
                source.syncData(ModDataAttachments.BEAM_XYZ_DATA.get());
            }
        } catch (Exception ignored) {
        }
    }
}