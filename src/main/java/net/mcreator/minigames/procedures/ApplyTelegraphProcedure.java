package net.mcreator.minigames.procedures;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.mcreator.minigames.entity.FlavioTrapdoor2Entity;
import net.mcreator.minigames.entity.FlavioTrapdoor3Entity;
import net.mcreator.minigames.entity.FlavioTrapdoorEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(
        modid = "minigames",
        value = Dist.CLIENT
)
public class ApplyTelegraphProcedure {

    private static final List<Telegraph> TELEGRAPHS =
            new CopyOnWriteArrayList<>();

    public static void execute(
            LevelAccessor world,
            double x,
            double y,
            double z,
            int ticks,
            int attackId
    ) {
        if (!(world instanceof Level level) || !level.isClientSide())
            return;

        if (ticks <= 0)
            return;

        TELEGRAPHS.add(
                new Telegraph(
                        new Vec3(x, y, z),
                        ticks
                )
        );
    }

    @SubscribeEvent
    public static void onClientTick(
            ClientTickEvent.Post event
    ) {
        for (Telegraph telegraph : TELEGRAPHS) {
            telegraph.ticks--;

            if (telegraph.ticks <= 0) {
                TELEGRAPHS.remove(telegraph);
            }
        }
    }

    private static Entity getHighestEntity(
            Level level,
            double x,
            double y,
            double z
    ) {
        AABB searchBox = new AABB(
                x - 0.05D,
                level.getMinY(),
                z - 0.05D,
                x + 0.05D,
                y + 2.0D,
                z + 0.05D
        );

        Entity highestEntity = null;
        double highestY = -Double.MAX_VALUE;

        for (Entity entity : level.getEntities(
                (Entity) null,
                searchBox,
                e -> e instanceof FlavioTrapdoorEntity
                        || e instanceof FlavioTrapdoor2Entity
                        || e instanceof FlavioTrapdoor3Entity
        )) {
            AABB box = entity.getBoundingBox();

            if (x >= box.minX && x <= box.maxX
                    && z >= box.minZ && z <= box.maxZ
                    && box.maxY <= y + 2.0D
                    && box.maxY > highestY) {

                highestY = box.maxY;
                highestEntity = entity;
            }
        }

        return highestEntity;
    }

    private static BlockPos getHighestBlock(
            Level level,
            BlockPos start
    ) {
        BlockPos pos = start;

        while (pos.getY() >= level.getMinY()) {
            BlockState state = level.getBlockState(pos);

            if (!state.isAir())
                return pos;

            pos = pos.below();
        }

        return null;
    }

    private static void renderQuad(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ
    ) {
        float x1 = (float) minX;
        float y1 = (float) minY;
        float z1 = (float) minZ;

        float x2 = (float) maxX;
        float y2 = (float) maxY;
        float z2 = (float) maxZ;

        float alpha = 0.40F;

        consumer.addVertex(
                pose,
                x1,
                y1,
                z1
        ).setColor(
                1.0F,
                0.0F,
                0.0F,
                alpha
        );

        consumer.addVertex(
                pose,
                x2,
                y1,
                z1
        ).setColor(
                1.0F,
                0.0F,
                0.0F,
                alpha
        );

        consumer.addVertex(
                pose,
                x2,
                y1,
                z2
        ).setColor(
                1.0F,
                0.0F,
                0.0F,
                alpha
        );

        consumer.addVertex(
                pose,
                x1,
                y1,
                z2
        ).setColor(
                1.0F,
                0.0F,
                0.0F,
                alpha
        );
    }

    @SubscribeEvent
    public static void onRenderLevel(
            RenderLevelStageEvent.AfterTranslucentBlocks event
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level == null
                || minecraft.player == null
                || TELEGRAPHS.isEmpty()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();

        MultiBufferSource.BufferSource buffer =
                minecraft.renderBuffers().bufferSource();

        VertexConsumer consumer =
                buffer.getBuffer(
                        RenderTypes.debugQuads()
                );

        Vec3 camera =
                minecraft.gameRenderer
                        .getMainCamera()
                        .position();

        for (Telegraph telegraph : TELEGRAPHS) {

            double x = telegraph.pos.x;
            double y = telegraph.pos.y;
            double z = telegraph.pos.z;

            Entity entity =
                    getHighestEntity(
                            level,
                            x,
                            y,
                            z
                    );

            if (entity != null) {

                AABB box =
                        entity.getBoundingBox();

                double minX =
                        box.minX - camera.x;

                double maxX =
                        box.maxX - camera.x;

                double minY =
                        box.maxY - camera.y
                                + 0.015D;

                double maxY =
                        minY + 0.01D;

                double minZ =
                        box.minZ - camera.z;

                double maxZ =
                        box.maxZ - camera.z;

                poseStack.pushPose();

                renderQuad(
                        consumer,
                        poseStack.last(),
                        minX,
                        minY,
                        minZ,
                        maxX,
                        maxY,
                        maxZ
                );

                poseStack.popPose();

                continue;
            }

            BlockPos start =
                    BlockPos.containing(
                            x,
                            y,
                            z
                    );

            BlockPos pos =
                    getHighestBlock(
                            level,
                            start
                    );

            if (pos == null)
                continue;

            BlockState state =
                    level.getBlockState(pos);

            VoxelShape shape =
                    state.getShape(
                            level,
                            pos
                    );

            while (
                    shape.isEmpty()
                            && pos.getY() > level.getMinY()
            ) {
                pos = pos.below();

                state =
                        level.getBlockState(pos);

                shape =
                        state.getShape(
                                level,
                                pos
                        );
            }

            if (shape.isEmpty())
                continue;

            for (AABB box : shape.toAabbs()) {

                double minX =
                        pos.getX()
                                + box.minX
                                - camera.x;

                double maxX =
                        pos.getX()
                                + box.maxX
                                - camera.x;

                double minY =
                        pos.getY()
                                + box.maxY
                                - camera.y
                                + 0.015D;

                double maxY =
                        minY + 0.01D;

                double minZ =
                        pos.getZ()
                                + box.minZ
                                - camera.z;

                double maxZ =
                        pos.getZ()
                                + box.maxZ
                                - camera.z;

                poseStack.pushPose();

                renderQuad(
                        consumer,
                        poseStack.last(),
                        minX,
                        minY,
                        minZ,
                        maxX,
                        maxY,
                        maxZ
                );

                poseStack.popPose();
            }
        }

        buffer.endBatch(
                RenderTypes.debugQuads()
        );
    }

    private static class Telegraph {

        private final Vec3 pos;
        private int ticks;

        private Telegraph(
                Vec3 pos,
                int ticks
        ) {
            this.pos = pos;
            this.ticks = ticks;
        }
    }
}
