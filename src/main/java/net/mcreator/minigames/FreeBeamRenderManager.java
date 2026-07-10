package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class FreeBeamRenderManager {

    public static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/misc/white.png");
    private static final float BEAM_HEIGHT = 20.0f;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterParticles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        renderBeams(bufferSource, cameraPos, mc.level.getEntities((Entity) null, new AABB(cameraPos, cameraPos).inflate(256.0D), entity -> entity.hasData(ModDataAttachments.BEAM_DATA)), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }

    public static void renderBeams(MultiBufferSource bufferSource, Vec3 cameraPos, Iterable<? extends Entity> entities, float partialTick) {
        for (Entity source : entities) {
            try {
                ModDataAttachments.BeamData data = source.getData(ModDataAttachments.BEAM_DATA);
                if (data.hasBeam && source.tickCount - data.startTick <= data.durationTicks) {
                    Entity target = source.level().getEntity(data.targetId);
                    if (target == null || !target.isAlive()) {
                        continue;
                    }
                    Vec3 start = source.getEyePosition(partialTick);
                    Vec3 end = target.getEyePosition(partialTick);
                    renderBeamQuad(bufferSource, cameraPos, start, end, data.texture, source.tickCount + partialTick, data.scale);
                }
                if (source instanceof Player player) {
                    MobEffectInstance ascendingEffect = player.getEffect(MinigamesModMobEffects.ASCENDING);
                    MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
                    if (ascendingEffect != null || (vars != null && (vars.ascendingActive || vars.ascendingTimer > 0))) {
                        Vec3 start = player.getEyePosition(partialTick);
                        Vec3 end = start.add(0.0D, 20.0D, 0.0D);
                        renderBeamQuad(bufferSource, cameraPos, start, end, "minigames:textures/entities/zap.png", source.tickCount + partialTick, 1.25D);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void renderBeamQuad(MultiBufferSource bufferSource, Vec3 cameraPos, Vec3 startWorld, Vec3 endWorld, String texturePath, float tickTime, double scale) {
        ResourceLocation texture = texturePath == null || texturePath.isEmpty() ? WHITE_TEXTURE : ResourceLocation.parse(texturePath);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        Vec3 start = startWorld.subtract(cameraPos);
        Vec3 end = endWorld.subtract(cameraPos);
        Vec3 dir = end.subtract(start);
        if (dir.lengthSqr() < 1.0E-6D) {
            return;
        }
        dir = dir.normalize();

        Vec3 baseSide = dir.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (baseSide.lengthSqr() < 1.0E-6D) baseSide = dir.cross(new Vec3(1.0D, 0.0D, 0.0D));
        if (baseSide.lengthSqr() < 1.0E-6D) baseSide = new Vec3(0.0D, 0.0D, 1.0D);
        baseSide = baseSide.normalize();

        Vec3 baseUp = dir.cross(baseSide).normalize();
        Vec3 right = baseUp.scale(Math.max(0.001D, scale) * 0.08D);
        Vec3 up = baseSide.scale(Math.max(0.001D, scale) * 0.08D);
        double length = start.distanceTo(end);
        int segments = Math.max(1, Mth.ceil(length / 0.5D));
        float animationOffset = (tickTime * 0.05F) % 1.0F;

        for (int i = 0; i < segments; i++) {
            float a = (float) i / segments;
            float b = (float) (i + 1) / segments;
            Vec3 segStart = start.add(dir.scale(length * a));
            Vec3 segEnd = start.add(dir.scale(length * b));
            drawQuadBeam(buffer, segStart, segEnd, right, up, animationOffset, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void drawQuadBeam(VertexConsumer buffer, Vec3 start, Vec3 end, Vec3 right, Vec3 up, float u0, float u1, float r, float g, float b) {
        Vec3 startRight = start.add(right);
        Vec3 startLeft = start.subtract(right);
        Vec3 endRight = end.add(right);
        Vec3 endLeft = end.subtract(right);

        buffer.addVertex((float) startRight.x, (float) startRight.y, (float) startRight.z).setColor(r, g, b, 1.0F).setUv(u0, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal((float) up.x, (float) up.y, (float) up.z);
        buffer.addVertex((float) endRight.x, (float) endRight.y, (float) endRight.z).setColor(r, g, b, 1.0F).setUv(u1, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal((float) up.x, (float) up.y, (float) up.z);
        buffer.addVertex((float) endLeft.x, (float) endLeft.y, (float) endLeft.z).setColor(r, g, b, 1.0F).setUv(u1, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal((float) up.x, (float) up.y, (float) up.z);
        buffer.addVertex((float) startLeft.x, (float) startLeft.y, (float) startLeft.z).setColor(r, g, b, 1.0F).setUv(u0, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal((float) up.x, (float) up.y, (float) up.z);
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
