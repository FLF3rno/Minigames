package net.mcreator.minigames.procedures;

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


public class TeleportAchievementProcedure {
    private static final TagKey<Block> UNSAFE_TAG = TagKey.create(Registries.BLOCK, Identifier.parse("minigames:unsafe"));

    public static void execute(LevelAccessor world) {
        if (!(world instanceof ServerLevel currentLevel))
            return;

        ServerLevel overworld = currentLevel.getServer().getLevel(Level.OVERWORLD);
        if (overworld == null)
            return;

        BlockPos targetPos = null;
        RandomSource random = overworld.getRandom();

        for (int i = 0; i < 100; i++) {
            int x = Mth.nextInt(random, 1000, 10000000);
            int z = Mth.nextInt(random, 1000, 10000000);

            int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

            BlockPos groundPos = new BlockPos(x, y - 1, z);
            BlockState groundState = overworld.getBlockState(groundPos);

            if (!groundState.isAir() 
                && !groundState.is(Blocks.LAVA) 
                && !groundState.is(Blocks.WATER) 
                && !groundState.is(UNSAFE_TAG)) {
                
                targetPos = new BlockPos(x, y, z);
                break;
            }
        }

        if (targetPos == null) {
            targetPos = new BlockPos(1000, overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 1000, 1000), 1000);
        }

        double tpX = targetPos.getX() + 0.5;
        double tpY = targetPos.getY() + 0.1;
        double tpZ = targetPos.getZ() + 0.5;

        for (ServerPlayer player : overworld.getServer().getPlayerList().getPlayers()) {
            player.teleportTo(
                    tpX,
                tpY,
                tpZ
            );
        }

        if (overworld.getLevelData() instanceof WritableLevelData levelData) {
            levelData.setSpawn(LevelData.RespawnData.of(Level.OVERWORLD, targetPos, 0.0F, 0.0F));
        }
    }
}