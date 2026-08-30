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
    private static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/misc/white.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterTranslucentParticles event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return;
        }

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().position();

        for (Entity source : mc.level.getEntities(
                (Entity)null,
                new AABB(cameraPos, cameraPos).inflate(512.0D),
                e -> true
        )) {
            try {
                ModDataAttachments.BeamXYZData data =
                        source.getData(ModDataAttachments.BEAM_XYZ_DATA);

                if (!data.active) continue;

                System.out.println("XYZ BEAM ACTIVE");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Identifier getTexture(String path) {
        if (path == null || path.isEmpty()) return DEFAULT_TEXTURE;
        try {
            return Identifier.parse(path);
        } catch (Exception e) {
            return DEFAULT_TEXTURE;
        }
    }

    private static void renderBeam(MultiBufferSource.BufferSource buffers, Vec3 start, Vec3 end, double scale, String texturePath, boolean emissive, float tickTime) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        if (length < 0.001D) return;

        Vec3 forward = direction.normalize();
        Vec3 reference = Math.abs(forward.y) < 0.95D ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 side = reference.cross(forward).normalize().scale(Math.max(0.001D, scale) * 0.12D);
        Vec3 up = forward.cross(side).normalize().scale(Math.max(0.001D, scale) * 0.12D);

        RenderType renderType = RenderTypes.entityTranslucent(getTexture(texturePath));
        VertexConsumer buffer = buffers.getBuffer(renderType);

        int segments = Math.max(1, Mth.ceil(length / 0.5D));
        float animationOffset = (tickTime * 0.1F) % 1.0F;

        for (int i = 0; i < segments; i++) {
            double p0 = (double)i / segments;
            double p1 = (double)(i + 1) / segments;

            Vec3 a = start.add(forward.scale(length * p0));
            Vec3 b = start.add(forward.scale(length * p1));

            addQuad(buffer, a.add(side), b.add(side), b.subtract(side), a.subtract(side), (float)p0 - animationOffset, (float)p1 - animationOffset, emissive);
            addQuad(buffer, a.subtract(up), b.subtract(up), b.add(up), a.add(up), (float)p0 - animationOffset, (float)p1 - animationOffset, emissive);
            addQuad(buffer, a.add(up), b.add(up), b.add(side), a.add(side), (float)p0 - animationOffset, (float)p1 - animationOffset, emissive);
            addQuad(buffer, a.subtract(side), b.subtract(side), b.subtract(up), a.subtract(up), (float)p0 - animationOffset, (float)p1 - animationOffset, emissive);
        }
    }

    private static void renderSprite(MultiBufferSource.BufferSource buffers, Vec3 start, Vec3 end, double scale, String texturePath, boolean emissive, float tickTime) {
        Vec3 direction = end.subtract(start);
        double length = direction.length();
        if (length < 0.001D) return;

        Vec3 forward = direction.normalize();
        Vec3 reference = Math.abs(forward.y) < 0.95D ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 side = reference.cross(forward).normalize().scale(Math.max(0.001D, scale) * 0.12D);
        Vec3 up = forward.cross(side).normalize().scale(Math.max(0.001D, scale) * 0.12D);

        RenderType renderType = RenderTypes.entityTranslucent(getTexture(texturePath));
        VertexConsumer buffer = buffers.getBuffer(renderType);

        float offset = (tickTime * 0.1F) % 1.0F;

        addQuad(buffer, start.add(side), end.add(side), end.subtract(side), start.subtract(side), -offset, (float)length - offset, emissive);
        addQuad(buffer, start.add(up), end.add(up), end.subtract(up), start.subtract(up), -offset, (float)length - offset, emissive);
    }

    private static void addQuad(VertexConsumer buffer, Vec3 a, Vec3 b, Vec3 c, Vec3 d, float u0, float u1, boolean emissive) {
        int light = emissive ? 15728880 : 0;

        buffer.addVertex((float)a.x, (float)a.y, (float)a.z).setColor(1, 1, 1, 1).setUv(u0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex((float)b.x, (float)b.y, (float)b.z).setColor(1, 1, 1, 1).setUv(u1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex((float)c.x, (float)c.y, (float)c.z).setColor(1, 1, 1, 1).setUv(u1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex((float)d.x, (float)d.y, (float)d.z).setColor(1, 1, 1, 1).setUv(u0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 1, 0);
    }

    private static void clearBeamData(Entity source) {
        source.setData(ModDataAttachments.BEAM_XYZ_DATA, new ModDataAttachments.BeamXYZData(false, 0, 0, 0, 0, 0, 0, 0, 0, 0, "", "beam", false));
    }
}