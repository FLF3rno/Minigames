package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.util.AchievementStrongholdTargets;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleportAchievementProcedure {
    private static final TagKey<Block> UNSAFE_TAG = TagKey.create(Registries.BLOCK, Identifier.parse("minigames:unsafe"));

    public static void execute(LevelAccessor world) {
        if (!(world instanceof ServerLevel currentLevel)) return;
        ServerLevel overworld = currentLevel.getServer().getLevel(Level.OVERWORLD);
        if (overworld == null) return;

        BlockPos targetPos = null;
        RandomSource random = overworld.getRandom();

        for (int i = 0; i < 100; i++) {
            int x = Mth.nextInt(random, -10000000, 10000000);
            int z = Mth.nextInt(random, -10000000, 10000000);

            overworld.getChunkSource().getChunk(x >> 4, z >> 4, ChunkStatus.FULL, true);

            int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

            if (overworld.isOutsideBuildHeight(y)) {
                continue;
            }

            BlockPos groundPos = new BlockPos(x, y - 1, z);
            BlockState groundState = overworld.getBlockState(groundPos);

            if (!groundState.isAir() && !groundState.is(Blocks.LAVA) && !groundState.is(Blocks.WATER) && !groundState.is(UNSAFE_TAG)) {
                targetPos = new BlockPos(x, y, z);
                break;
            }
        }

        if (targetPos == null) {
            int fallbackX = 1000;
            int fallbackZ = 1000;
            overworld.getChunkSource().getChunk(fallbackX >> 4, fallbackZ >> 4, ChunkStatus.FULL, true);
            int fallbackY = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fallbackX, fallbackZ);
            targetPos = new BlockPos(fallbackX, Math.max(fallbackY, 64), fallbackZ);
        }

        int centerChunkX = targetPos.getX() >> 4;
        int centerChunkZ = targetPos.getZ() >> 4;
        for (int cx = centerChunkX - 1; cx <= centerChunkX + 1; cx++) {
            for (int cz = centerChunkZ - 1; cz <= centerChunkZ + 1; cz++) {
                overworld.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, true);
            }
        }

        MinigamesModVariables.MapVariables.get(world).coordinateOffset = new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ());

        List<BlockPos> targets = new ArrayList<>();
        int ringCount = 3;
        int ringDistance = 192;
        int ringSize = 3;
        for (int ring = 1; ring <= ringCount; ring++) {
            int distance = ringDistance * ring;
            for (int i = 0; i < ringSize; i++) {
                double angle = (Math.PI * 2.0 * i / ringSize) + (ring * 0.73D);
                int x = targetPos.getX() + (int) Math.round(Math.cos(angle) * distance);
                int z = targetPos.getZ() + (int) Math.round(Math.sin(angle) * distance);
                int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                targets.add(new BlockPos(x, Math.max(y, targetPos.getY()), z));
            }
            ringSize += 2;
        }
        AchievementStrongholdTargets.setTargets(targets);
        double tpX = targetPos.getX() + 0.5;
        double tpY = targetPos.getY() + 1.0;
        double tpZ = targetPos.getZ() + 0.5;

        for (ServerPlayer player : overworld.getServer().getPlayerList().getPlayers()) {
            player.teleportTo(
                    overworld,
                    tpX,
                    tpY,
                    tpZ,
                    Collections.emptySet(),
                    player.getYRot(),
                    player.getXRot(),
                    true
            );
        }

        if (overworld.getLevelData() instanceof WritableLevelData levelData) {
            levelData.setSpawn(LevelData.RespawnData.of(Level.OVERWORLD, targetPos, 0.0F, 0.0F));
        }
    }
}
