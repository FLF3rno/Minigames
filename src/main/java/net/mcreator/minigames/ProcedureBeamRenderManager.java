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
public class ProcedureBeamRenderManager {

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

        MultiBufferSource.BufferSource bufferSource =
                mc.renderBuffers().bufferSource();

        float partialTick =
                mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        for (Entity source : mc.level.getEntities(
                (Entity) null,
                new AABB(cameraPos, cameraPos).inflate(256.0D),
                entity -> entity.hasData(ModDataAttachments.BEAM_DATA))) {

            try {
                ModDataAttachments.BeamData data =
                        source.getData(ModDataAttachments.BEAM_DATA);

                if (data == null || !data.hasBeam) {
                    continue;
                }

                if (!source.isAlive()
                        || (source.tickCount - data.startTick) > data.durationTicks) {

                    clearBeamData(source);
                    continue;
                }

                Entity target = source.level().getEntity(data.targetId);

                if (target == null || !target.isAlive()) {
                    clearBeamData(source);
                    continue;
                }

                Vec3 start = getBodyCenter(source, partialTick);
                Vec3 end = getBodyCenter(target, partialTick);

                renderRibbon(
                        bufferSource,
                        cameraPos,
                        start,
                        end,
                        data.texture,
                        source.tickCount + partialTick,
                        data.scale
                );

            } catch (Exception ignored) {
            }
        }

        bufferSource.endBatch();
    }

    private static Vec3 getBodyCenter(Entity entity, float partialTick) {

        double x = Mth.lerp(
                partialTick,
                entity.xo,
                entity.getX()
        );

        double y = Mth.lerp(
                partialTick,
                entity.yo,
                entity.getY()
        ) + entity.getBbHeight() * 0.5D;

        double z = Mth.lerp(
                partialTick,
                entity.zo,
                entity.getZ()
        );

        return new Vec3(x, y, z);
    }

    private static void clearBeamData(Entity source) {

        try {

            source.setData(
                    ModDataAttachments.BEAM_DATA,
                    new ModDataAttachments.BeamData(
                            false,
                            -1,
                            0,
                            0,
                            0.0D,
                            "",
                            0,
                            0,
                            0
                    )
            );

            if (!source.level().isClientSide()) {
                source.syncData(ModDataAttachments.BEAM_DATA.get());
            }

        } catch (Exception ignored) {
        }
    }

    private static void renderRibbon(
            MultiBufferSource.BufferSource bufferSource,
            Vec3 cameraPos,
            Vec3 startWorld,
            Vec3 endWorld,
            String texturePath,
            float tickTime,
            double scale
    ) {

        Identifier texture;

        if (texturePath == null || texturePath.isEmpty()) {
            texture = DEFAULT_TEXTURE;
        } else {
            try {
                texture = Identifier.parse(texturePath);
            } catch (Exception e) {
                texture = DEFAULT_TEXTURE;
            }
        }

        RenderType renderType = RenderTypes.entityTranslucent(texture);

        VertexConsumer buffer = bufferSource.getBuffer(renderType);


        Vec3 start = startWorld.subtract(cameraPos);
        Vec3 end = endWorld.subtract(cameraPos);

        Vec3 dir = end.subtract(start);

        if (dir.lengthSqr() < 1.0E-6D) {
            return;
        }

        Vec3 forward = dir.normalize();

        Vec3 reference;

        if (Math.abs(forward.y) < 0.95D) {
            reference = new Vec3(0.0D, 1.0D, 0.0D);
        } else {
            reference = new Vec3(1.0D, 0.0D, 0.0D);
        }

        Vec3 side = reference.cross(forward);

        if (side.lengthSqr() < 1.0E-6D) {
            side = new Vec3(0.0D, 0.0D, 1.0D).cross(forward);
        }

        side = side.normalize()
                .scale(Math.max(0.001D, scale) * 0.12D);

        Vec3 up = forward.cross(side)
                .normalize()
                .scale(Math.max(0.001D, scale) * 0.08D);

        double length = start.distanceTo(end);

        int segments = Math.max(
                1,
                Mth.ceil(length / 0.5D)
        );

        float animationOffset = (tickTime * 0.1F) % 1.0F;

        for (int i = 0; i < segments; i++) {

            double startPercent = (double) i / segments;
            double endPercent = (double) (i + 1) / segments;

            Vec3 segStart =
                    start.add(forward.scale(length * startPercent));

            Vec3 segEnd =
                    start.add(forward.scale(length * endPercent));

            float u0 =
                    (float) startPercent - animationOffset;

            float u1 =
                    (float) endPercent - animationOffset;

            drawPrism(
                    buffer,
                    segStart,
                    segEnd,
                    side,
                    up,
                    u0,
                    u1
            );
        }
    }

    private static void drawPrism(
            VertexConsumer buffer,
            Vec3 start,
            Vec3 end,
            Vec3 side,
            Vec3 up,
            float u0,
            float u1
    ) {

        Vec3 sR = start.add(side);
        Vec3 sL = start.subtract(side);

        Vec3 sU = start.add(up);
        Vec3 sD = start.subtract(up);

        Vec3 eR = end.add(side);
        Vec3 eL = end.subtract(side);

        Vec3 eU = end.add(up);
        Vec3 eD = end.subtract(up);

        /*
         * Four sides of the beam.
         */

        addQuad(
                buffer,
                sR,
                eR,
                eL,
                sL,
                u0,
                u1
        );

        addQuad(
                buffer,
                sD,
                eD,
                eU,
                sU,
                u0,
                u1
        );

        addQuad(
                buffer,
                sU,
                eU,
                eR,
                sR,
                u0,
                u1
        );

        addQuad(
                buffer,
                sL,
                eL,
                eD,
                sD,
                u0,
                u1
        );
    }

    private static void addQuad(
            VertexConsumer buffer,
            Vec3 a,
            Vec3 b,
            Vec3 c,
            Vec3 d,
            float u0,
            float u1
    ) {

        buffer.addVertex(
                        (float) a.x,
                        (float) a.y,
                        (float) a.z
                )
                .setColor(
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F
                )
                .setUv(
                        u0,
                        1.0F
                )
                .setOverlay(
                        OverlayTexture.NO_OVERLAY
                )
                .setLight(
                        15728880
                )
                .setNormal(
                        0,
                        1,
                        0
                );

        buffer.addVertex(
                        (float) b.x,
                        (float) b.y,
                        (float) b.z
                )
                .setColor(
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F
                )
                .setUv(
                        u1,
                        1.0F
                )
                .setOverlay(
                        OverlayTexture.NO_OVERLAY
                )
                .setLight(
                        15728880
                )
                .setNormal(
                        0,
                        1,
                        0
                );

        buffer.addVertex(
                        (float) c.x,
                        (float) c.y,
                        (float) c.z
                )
                .setColor(
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F
                )
                .setUv(
                        u1,
                        0.0F
                )
                .setOverlay(
                        OverlayTexture.NO_OVERLAY
                )
                .setLight(
                        15728880
                )
                .setNormal(
                        0,
                        1,
                        0
                );

        buffer.addVertex(
                        (float) d.x,
                        (float) d.y,
                        (float) d.z
                )
                .setColor(
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F
                )
                .setUv(
                        u0,
                        0.0F
                )
                .setOverlay(
                        OverlayTexture.NO_OVERLAY
                )
                .setLight(
                        15728880
                )
                .setNormal(
                        0,
                        1,
                        0
                );
    }
}