package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class HealthBar {
    private static final TagKey<EntityType<?>> DUNGEON_TAG = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("minigames", "dungeon")
    );
    private static final TagKey<EntityType<?>> DUNGEON_BOSS_TAG = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath("minigames", "dungeon_boss")
    );

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterOpaqueFeatures event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPosition = camera.position();
        double camX = cameraPosition.x;
        double camY = cameraPosition.y;
        double camZ = cameraPosition.z;

        for (LivingEntity entity : mc.level.getEntitiesOfClass(
                LivingEntity.class,
                mc.player.getBoundingBox().inflate(32.0)
        )) {
            if (entity == mc.player || !entity.isAlive()) continue;

            if (!entity.is(DUNGEON_TAG)) continue;
            if (entity.is(DUNGEON_BOSS_TAG)) continue;

            if (!shouldShowHealthBar(mc, entity)) continue;

            double x = entity.getX() - camX;
            double y = entity.getY() + entity.getBbHeight() + 0.7 - camY;
            double z = entity.getZ() - camZ;

            poseStack.pushPose();
            poseStack.translate(x, y, z);

            poseStack.mulPose(camera.rotation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            PoseStack.Pose pose = poseStack.last();
            if (pose != null) {
                Matrix4f matrix = pose.pose();
                renderBar(matrix, entity, entity.getHealth(), entity.getMaxHealth());
            }

            poseStack.popPose();
        }
    }

    private static boolean shouldShowHealthBar(Minecraft mc, LivingEntity entity) {
        if (isPlayerLookingAtEntity(mc, entity)) return true;

        long currentTime = System.currentTimeMillis();
        long lastDamageTime = entity.getPersistentData().getLong("healthbarLastDamageTime").orElse(0L);
        long timeSinceDamage = currentTime - lastDamageTime;

        return timeSinceDamage < 4000;
    }

    private static boolean isPlayerLookingAtEntity(Minecraft mc, LivingEntity entity) {
        var camera = mc.getCameraEntity();
        if (camera == null || mc.level == null) return false;

        double range = 1000.0D;

        Vec3 start = camera.getEyePosition(1.0F);
        Vec3 end = start.add(camera.getViewVector(1.0F).scale(range));

        BlockHitResult blockHit = mc.level.clip(
                new ClipContext(
                        start,
                        end,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        camera
                )
        );

        double blockDistance = blockHit == null
                ? Double.POSITIVE_INFINITY
                : blockHit.getLocation().distanceTo(start);

        AABB searchBox = camera.getBoundingBox()
                .expandTowards(camera.getViewVector(1.0F).scale(range))
                .inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                camera,
                start,
                end,
                searchBox,
                e -> e instanceof LivingEntity && e.isPickable(),
                range * range
        );

        if (entityHit == null) return false;

        double entityDistance = entityHit.getLocation().distanceTo(start);

        return entityHit.getEntity() == entity
                && entityDistance <= blockDistance + 1e-6;
    }

    private static void renderBar(Matrix4f matrix, LivingEntity entity, float health, float maxHealth) {
        float healthRatio = Math.max(0.0F, Math.min(health / maxHealth, 1.0F));

        int width = 60;
        int height = 5;
        int halfWidth = width / 2;

        int fillWidth = (int) (width * healthRatio);

        int r;
        int g;
        int b;

        String override = "";

        String rawOverride = "";

        try {
            if (entity.getPersistentData().contains("healthbarColor")) {
                rawOverride = entity.getPersistentData()
                        .getString("healthbarColor")
                        .orElse("");
            } else if (entity.getPersistentData().contains("healthbarColorInt")) {
                int hexInt = entity.getPersistentData()
                        .getInt("healthbarColorInt")
                        .orElse(0);

                rawOverride = String.format("%06X", hexInt & 0xFFFFFF);
            }
        } catch (Exception ignored) {
        }

        override = rawOverride == null ? "" : rawOverride.trim();

        if (override.startsWith("\"")
                && override.endsWith("\"")
                && override.length() > 1) {
            override = override.substring(1, override.length() - 1);
        }

        if (override.startsWith("0x") || override.startsWith("0X")) {
            override = override.substring(2);
        }

        if (override.startsWith("#")) {
            override = override.substring(1);
        }

        if (override.length() == 3) {
            override =
                    "" + override.charAt(0) + override.charAt(0)
                            + override.charAt(1) + override.charAt(1)
                            + override.charAt(2) + override.charAt(2);
        }

        if (override.length() == 8) {
            override = override.substring(2);
        }

        boolean parsed = false;
        int parsedHex = 0;

        if (override.matches("^[0-9a-fA-F]{6}$")) {
            parsedHex = Integer.parseInt(override, 16);

            r = (parsedHex >> 16) & 0xFF;
            g = (parsedHex >> 8) & 0xFF;
            b = parsedHex & 0xFF;

            parsed = true;
        } else {
            if (healthRatio >= 0.99f) {
                r = 0;
                g = 100;
                b = 0;
            } else if (healthRatio >= 0.75f) {
                r = 0;
                g = 200;
                b = 0;
            } else if (healthRatio >= 0.5f) {
                r = 100;
                g = 100;
                b = 0;
            } else if (healthRatio >= 0.25f) {
                r = 200;
                g = 100;
                b = 0;
            } else {
                r = 255;
                g = 0;
                b = 0;
            }
        }

        MultiBufferSource.BufferSource buffers =
                Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer bgConsumer = buffers.getBuffer(RenderTypes.debugQuads());

        drawQuad(
                bgConsumer,
                matrix,
                -halfWidth - 1,
                -1,
                halfWidth + 1,
                height + 1,
                0,
                0,
                0,
                180,
                -0.02f
        );

        buffers.endBatch(RenderTypes.debugQuads());

        if (fillWidth > 0) {
            VertexConsumer fillConsumer = buffers.getBuffer(RenderTypes.debugQuads());

            drawQuad(
                    fillConsumer,
                    matrix,
                    halfWidth - fillWidth,
                    0,
                    halfWidth,
                    height,
                    r,
                    g,
                    b,
                    255,
                    0.02f
            );

            buffers.endBatch(RenderTypes.debugQuads());
        }

    }

    private static void drawQuad(
            VertexConsumer vertexConsumer,
            Matrix4f matrix,
            float x1,
            float y1,
            float x2,
            float y2,
            int r,
            int g,
            int b,
            int a,
            float z
    ) {
        int color = (a << 24) | (r << 16) | (g << 8) | b;

        vertexConsumer.addVertex(matrix, x1, y1, z).setColor(color);
        vertexConsumer.addVertex(matrix, x1, y2, z).setColor(color);
        vertexConsumer.addVertex(matrix, x2, y2, z).setColor(color);
        vertexConsumer.addVertex(matrix, x2, y1, z).setColor(color);
    }
}