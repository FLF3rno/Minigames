package net.mcreator.minigames.mixin;

import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;

@Mixin(DebugEntryPosition.class)
public class DebugEntryPositionMixin {

    @Inject(
            method = "display",
            at = @At("HEAD"),
            cancellable = true
    )
    private void display(
            DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk, CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.getCameraEntity();

        if (entity == null) {
            ci.cancel();
            return;
        }

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        var mapVariables = MinigamesModVariables.MapVariables.get(minecraft.level);
        double offsetX = mapVariables.coordinateOffset.x;
        double offsetZ = mapVariables.coordinateOffset.z;

        if (entity.level().dimension() == Level.OVERWORLD) {
        x -= offsetX;
        z -= offsetZ;
        }
        else if (entity.level().dimension() == Level.NETHER) {
            x = ((x * 8) - offsetX) / 8;
            z = ((z * 8) - offsetZ) / 8;
        }

        BlockPos feetPos = BlockPos.containing(x, y, z);
        ChunkPos chunkPos = ChunkPos.containing(feetPos);

        Direction direction = entity.getDirection();

        String faceString = switch (direction) {
            case NORTH -> "Towards negative Z";
            case SOUTH -> "Towards positive Z";
            case WEST -> "Towards negative X";
            case EAST -> "Towards positive X";
            default -> "Invalid";
        };

        LongSet chunks = (serverOrClientLevel instanceof ServerLevel serverLevel)
                ? serverLevel.getForceLoadedChunks()
                : LongSets.EMPTY_SET;

        displayer.addToGroup(
                DebugEntryPosition.GROUP,
                List.of(
                        String.format(
                                Locale.ROOT,
                                "XYZ: %.3f / %.5f / %.3f",
                                x,
                                y,
                                z
                        ),
                        String.format(
                                Locale.ROOT,
                                "Block: %d %d %d",
                                feetPos.getX(),
                                feetPos.getY(),
                                feetPos.getZ()
                        ),
                        String.format(
                                Locale.ROOT,
                                "Chunk: %d %d %d [%d %d in r.%d.%d.mca]",
                                chunkPos.x(),
                                SectionPos.blockToSectionCoord(feetPos.getY()),
                                chunkPos.z(),
                                chunkPos.getRegionLocalX(),
                                chunkPos.getRegionLocalZ(),
                                chunkPos.getRegionX(),
                                chunkPos.getRegionZ()
                        ),
                        String.format(
                                Locale.ROOT,
                                "Facing: %s (%s) (%.1f / %.1f)",
                                direction,
                                faceString,
                                Mth.wrapDegrees(entity.getYRot()),
                                Mth.wrapDegrees(entity.getXRot())
                        ),
                        minecraft.level.dimension().identifier() + " FC: " + chunks.size()
                )
        );

        ci.cancel();
    }
}
