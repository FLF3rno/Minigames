package net.mcreator.minigames.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class DimensionResetHelper {

    public static void resetRunDimensions(MinecraftServer server) {
        if (server == null) {
            return;
        }

        for (ServerLevel level : server.getAllLevels()) {
            ResourceKey<Level> dim = level.dimension();
            if (dim != Level.OVERWORLD && dim != Level.NETHER) {
                resetDimension(server, dim);
            }
        }
    }

    public static void resetEnd(MinecraftServer server) {
        resetDimension(server, Level.END);
    }

    public static void resetModdedDimension(MinecraftServer server, String dimensionId) {
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, Identifier.parse(dimensionId));
        resetDimension(server, key);
    }

    public static void resetDimension(MinecraftServer server, ResourceKey<Level> dimensionKey) {
        if (server == null || dimensionKey == null) {
            return;
        }

        ServerLevel level = server.getLevel(dimensionKey);
        if (level == null || !level.players().isEmpty()) {
            return;
        }

        level.getAllEntities().forEach(Entity::discard);

        if (dimensionKey == Level.END) {
            EnderDragonFight defaultFight = EnderDragonFight.createDefault();
            defaultFight.init(level, level.getSeed(), net.minecraft.core.BlockPos.ZERO);
            level.setDragonFight(defaultFight);
        }

        try {
            level.getChunkSource().save(false);
        } catch (Exception ignored) {
        }

        Path rootPath = server.getWorldPath(LevelResource.ROOT);
        Path dimFolder = DimensionType.getStorageFolder(dimensionKey, rootPath);
        Util.ioPool().execute(() -> {
            deleteSubfolder(dimFolder.resolve("region"));
            deleteSubfolder(dimFolder.resolve("entities"));
            deleteSubfolder(dimFolder.resolve("poi"));
        });
    }

    private static void deleteSubfolder(Path folder) {
        if (folder == null || !Files.exists(folder)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.sorted(Comparator.reverseOrder())
                 .filter(path -> !path.equals(folder))
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException ignored) {
                     }
                 });
        } catch (IOException ignored) {
        }
    }
}
