package net.mcreator.dungeondev.client;

import net.mcreator.dungeondev.DungeonDevMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

import java.util.Map;

public class DevWorldHelper {
    public static final String WORLD_NAME = "dungeon_rooms_dev";
    public static final String DISPLAY_NAME = "Dungeon Rooms Dev";

    public static void openOrCreateDevWorld(Minecraft mc, Screen parentScreen) {
        try {
            if (mc.getLevelSource().levelExists(WORLD_NAME)) {
                DungeonDevMod.LOGGER.info("Opening existing dev world: {}", WORLD_NAME);
                mc.createWorldOpenFlows().openWorld(WORLD_NAME, () -> mc.setScreen(parentScreen));
            } else {
                DungeonDevMod.LOGGER.info("Creating fresh empty dev world: {}", WORLD_NAME);
                LevelSettings settings = new LevelSettings(
                    DISPLAY_NAME,
                    GameType.CREATIVE,
                    new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false),
                    true, // allowCommands
                    WorldDataConfiguration.DEFAULT
                );
                WorldOptions options = new WorldOptions(0L, false, false);

                mc.createWorldOpenFlows().createFreshLevel(
                    WORLD_NAME,
                    settings,
                    options,
                    provider -> {
                        try {
                            HolderGetter<FlatLevelGeneratorPreset> presets = provider.lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET);
                            FlatLevelGeneratorPreset voidPreset = presets.getOrThrow(FlatLevelGeneratorPresets.THE_VOID).value();
                            var flatSettings = voidPreset.settings();
                            var chunkGenerator = new FlatLevelSource(flatSettings);
                            HolderGetter<DimensionType> dimTypes = provider.lookupOrThrow(Registries.DIMENSION_TYPE);
                            var overworldStem = new LevelStem(dimTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD), chunkGenerator);
                            return new WorldDimensions(Map.of(LevelStem.OVERWORLD, overworldStem));
                        } catch (Exception e) {
                            DungeonDevMod.LOGGER.warn("Failed creating custom void dimensions, falling back to flat preset", e);
                            return WorldPresets.createFlatWorldDimensions(provider);
                        }
                    },
                    parentScreen
                );
            }
        } catch (Exception e) {
            DungeonDevMod.LOGGER.error("Failed to open or create dev world", e);
        }
    }
}
