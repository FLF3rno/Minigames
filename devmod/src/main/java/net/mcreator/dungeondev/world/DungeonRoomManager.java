package net.mcreator.dungeondev.world;

import net.mcreator.dungeondev.DungeonDevMod;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class DungeonRoomManager {

    public record RoomInfo(Identifier id, String type, Vec3i size, BlockPos sbPos, Vec3 tpPos) {}

    private static final Map<String, RoomInfo> CACHED_ROOMS = new LinkedHashMap<>();

    private static final Set<String> ROOM_SHAPE_DESCRIPTORS = Set.of(
        "start", "end", "boss", "miniboss", "loot", "one", "two", "three", "four", "five", "six",
        "room", "hall", "hallway", "corridor", "stairs", "stairway", "corner", "cross", "gate",
        "deadend", "entrance", "exit"
    );

    /**
     * Extracts the room type / theme (e.g., Church, Generic, Crypt, Catacombs)
     * adapting dynamically to any naming pattern.
     */
    public static String extractType(Identifier id) {
        String clean = id.getPath().toLowerCase();
        if (clean.startsWith("dungeon_")) {
            clean = clean.substring("dungeon_".length());
        }
        List<String> parts = new ArrayList<>(Arrays.asList(clean.split("_")));
        while (!parts.isEmpty() && parts.get(parts.size() - 1).matches("\\d+")) {
            parts.remove(parts.size() - 1);
        }
        if (parts.isEmpty()) {
            return "General";
        }

        // If the last token is not a shape descriptor, it represents the theme/type (e.g. 'church', 'generic')
        String last = parts.get(parts.size() - 1);
        if (!ROOM_SHAPE_DESCRIPTORS.contains(last)) {
            return capitalize(last);
        }

        // Otherwise look for any token from right to left that is not a descriptor
        for (int i = parts.size() - 1; i >= 0; i--) {
            if (!ROOM_SHAPE_DESCRIPTORS.contains(parts.get(i))) {
                return capitalize(parts.get(i));
            }
        }

        return capitalize(parts.get(0));
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return "General";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }

    /**
     * Finds all structure templates with 'dungeon_' prefix from registered resources,
     * workspace files, and world saves.
     */
    public static Set<Identifier> findDungeonStructures(ServerLevel level) {
        Set<Identifier> result = new TreeSet<>(Comparator.comparing(Identifier::toString));
        StructureTemplateManager structureManager = level.getStructureManager();

        // 1. Check registered templates in StructureTemplateManager
        try {
            structureManager.listTemplates().forEach(id -> {
                String path = id.getPath().toLowerCase();
                if (path.startsWith("dungeon_") || path.contains("/dungeon_")) {
                    result.add(id);
                }
            });
        } catch (Exception e) {
            DungeonDevMod.LOGGER.error("Failed to list templates from StructureTemplateManager", e);
        }

        // 2. Scan filesystem for newly added or local .nbt files
        String levelName = level.getServer().getWorldData().getLevelName();
        List<File> searchDirs = List.of(
            new File("src/main/resources/data/minigames/structure"),
            new File("src/main/resources/data/minigames/structures"),
            new File("run/saves/dungeon_rooms_dev/generated/minigames/structures"),
            new File("run/saves/dungeon_rooms_dev/generated/minecraft/structures"),
            new File("run/saves/" + levelName + "/generated/minigames/structures"),
            new File("saves/" + levelName + "/generated/minigames/structures")
        );

        for (File dir : searchDirs) {
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((d, name) -> name.toLowerCase().startsWith("dungeon_") && name.endsWith(".nbt"));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName();
                        String structName = name.substring(0, name.length() - 4);
                        Identifier id = Identifier.fromNamespaceAndPath("minigames", structName);
                        result.add(id);

                        // Load / refresh into structureManager from disk
                        try {
                            CompoundTag tag = NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
                            StructureTemplate template = structureManager.getOrCreate(id);
                            template.load(level.holderLookup(Registries.BLOCK), tag);
                            DungeonDevMod.LOGGER.info("Loaded structure template from disk: {}", file.getName());
                        } catch (Exception ex) {
                            DungeonDevMod.LOGGER.warn("Failed loading NBT file: " + file.getName(), ex);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Computes the deterministic grid layout for all dungeon rooms.
     */
    public static Map<String, RoomInfo> getOrComputeRooms(ServerLevel level) {
        if (!CACHED_ROOMS.isEmpty()) {
            return CACHED_ROOMS;
        }

        Set<Identifier> structures = findDungeonStructures(level);
        StructureTemplateManager structureManager = level.getStructureManager();
        int currentX = 0;
        int currentZ = 0;
        int startY = 60;
        int maxRowZ = 0;
        int spacing = 16;

        for (Identifier id : structures) {
            Optional<StructureTemplate> opt = structureManager.get(id);
            if (opt.isEmpty()) continue;
            StructureTemplate template = opt.get();
            Vec3i size = template.getSize();
            if (size.getX() <= 0 || size.getY() <= 0 || size.getZ() <= 0) continue;

            if (currentX + size.getX() > 280 && currentX > 0) {
                currentX = 0;
                currentZ += maxRowZ + spacing;
                maxRowZ = 0;
            }

            BlockPos sbPos = new BlockPos(currentX, startY, currentZ);
            Vec3 tpPos = new Vec3(sbPos.getX() + 0.5, startY + 1.0, sbPos.getZ() - 1.5);
            String type = extractType(id);

            RoomInfo info = new RoomInfo(id, type, size, sbPos, tpPos);
            CACHED_ROOMS.put(id.toString(), info);
            CACHED_ROOMS.put(id.getPath(), info);

            currentX += size.getX() + spacing;
            if (size.getZ() > maxRowZ) {
                maxRowZ = size.getZ();
            }
        }

        return CACHED_ROOMS;
    }

    /**
     * Clears an entire room's volume, platform, sign, and non-player entities.
     * Used on reload so existing structures are cleanly wiped before placing new ones,
     * preventing any ghost blocks when structures change without air blocks.
     */
    public static void clearRoomArea(ServerLevel level, BlockPos sbPos, Vec3i size) {
        int clearX = size.getX();
        int clearY = Math.max(size.getY(), 64);
        int clearZ = size.getZ();

        if (level.getBlockEntity(sbPos) instanceof StructureBlockEntity sbe) {
            Vec3i sbeSize = sbe.getStructureSize();
            if (sbeSize.getX() > clearX) clearX = sbeSize.getX();
            if (sbeSize.getY() > clearY) clearY = sbeSize.getY();
            if (sbeSize.getZ() > clearZ) clearZ = sbeSize.getZ();
        }

        BlockPos structPos = sbPos.above();
        BlockState airState = Blocks.AIR.defaultBlockState();

        // 1. Determine highest non-air block upwards all the way to world build ceiling
        int maxY = structPos.getY() + clearY;
        for (int x = structPos.getX(); x < structPos.getX() + clearX; x++) {
            for (int z = structPos.getZ(); z < structPos.getZ() + clearZ; z++) {
                int h = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                if (h > maxY) {
                    maxY = h;
                }
            }
        }
        maxY = Math.min(maxY, level.getMaxY() - 1);

        // 2. Clear structure volume all the way upwards to highest block / build limit
        BlockPos corner1 = structPos;
        BlockPos corner2 = new BlockPos(structPos.getX() + clearX - 1, maxY, structPos.getZ() + clearZ - 1);
        for (BlockPos p : BlockPos.betweenClosed(corner1, corner2)) {
            if (!level.getBlockState(p).isAir()) {
                level.setBlock(p, airState, 2);
            }
        }

        // 3. Clear non-player entities inside the structure area all the way upwards
        AABB aabb = new AABB(
            structPos.getX() - 1, structPos.getY(), structPos.getZ() - 1,
            structPos.getX() + clearX + 1, level.getMaxY(), structPos.getZ() + clearZ + 1
        );
        List<Entity> entities = level.getEntities((Entity) null, aabb, e -> !(e instanceof Player));
        for (Entity entity : entities) {
            entity.discard();
        }

        // 3. Clear sign in front
        BlockPos signPos = sbPos.offset(0, 1, -1);
        if (!level.getBlockState(signPos).isAir()) {
            level.setBlock(signPos, airState, 2);
        }

        // 4. Clear stone platform and border
        for (int dx = -2; dx <= clearX + 1; dx++) {
            for (int dz = -2; dz <= clearZ + 1; dz++) {
                BlockPos floorPos = sbPos.offset(dx, 0, dz);
                if (!level.getBlockState(floorPos).isAir()) {
                    level.setBlock(floorPos, airState, 2);
                }
            }
        }
    }

    /**
     * Generates or reloads all dungeon_ structures in a spacious, organized grid.
     * Each room has a Structure Block placed underneath it in SAVE mode,
     * with identical structureName, size, and offset so it perfectly envelops the room above it.
     */
    public static int generateRooms(ServerLevel level, @Nullable ServerPlayer player, boolean isReload) {
        if (isReload) {
            Map<String, RoomInfo> oldRooms = new LinkedHashMap<>(getOrComputeRooms(level));
            Set<RoomInfo> uniqueOldRooms = new HashSet<>(oldRooms.values());
            for (RoomInfo oldInfo : uniqueOldRooms) {
                clearRoomArea(level, oldInfo.sbPos(), oldInfo.size());
            }
        }

        CACHED_ROOMS.clear();
        Set<Identifier> structures = findDungeonStructures(level);
        if (structures.isEmpty()) {
            if (player != null) {
                player.sendSystemMessage(Component.literal("§c[Rooms Dev] No structures with prefix 'dungeon_' found!"));
            }
            return 0;
        }

        StructureTemplateManager structureManager = level.getStructureManager();
        int currentX = 0;
        int currentZ = 0;
        int startY = 60;
        int maxRowZ = 0;
        int spacing = 16;
        int placedCount = 0;

        for (Identifier id : structures) {
            Optional<StructureTemplate> opt = structureManager.get(id);
            if (opt.isEmpty()) {
                continue;
            }
            StructureTemplate template = opt.get();
            Vec3i size = template.getSize();
            if (size.getX() <= 0 || size.getY() <= 0 || size.getZ() <= 0) {
                continue;
            }

            // Wrap to next row if current row exceeds 280 blocks width
            if (currentX + size.getX() > 280 && currentX > 0) {
                currentX = 0;
                currentZ += maxRowZ + spacing;
                maxRowZ = 0;
            }

            BlockPos sbPos = new BlockPos(currentX, startY, currentZ);
            BlockPos structPos = sbPos.above();
            Vec3 tpPos = new Vec3(sbPos.getX() + 0.5, startY + 1.0, sbPos.getZ() - 1.5);
            String type = extractType(id);

            // Clear any existing blocks at this slot before placing the new structure
            clearRoomArea(level, sbPos, size);

            RoomInfo info = new RoomInfo(id, type, size, sbPos, tpPos);
            CACHED_ROOMS.put(id.toString(), info);
            CACHED_ROOMS.put(id.getPath(), info);

            // 1. Build a solid stone platform with perimeter around and under the room
            BlockState floorState = Blocks.POLISHED_ANDESITE.defaultBlockState();
            BlockState borderState = Blocks.CHISELED_STONE_BRICKS.defaultBlockState();
            BlockState lightState = Blocks.SEA_LANTERN.defaultBlockState();

            for (int dx = -2; dx <= size.getX() + 1; dx++) {
                for (int dz = -2; dz <= size.getZ() + 1; dz++) {
                    BlockPos floorPos = sbPos.offset(dx, 0, dz);
                    if (dx == 0 && dz == 0) {
                        continue; // Slot for Structure Block
                    }
                    boolean isCorner = (dx == -2 || dx == size.getX() + 1) && (dz == -2 || dz == size.getZ() + 1);
                    boolean isBorder = (dx == -2 || dx == size.getX() + 1 || dz == -2 || dz == size.getZ() + 1);

                    if (isCorner) {
                        level.setBlock(floorPos, lightState, 2);
                    } else if (isBorder) {
                        level.setBlock(floorPos, borderState, 2);
                    } else {
                        level.setBlock(floorPos, floorState, 2);
                    }
                }
            }

            // 2. Place Structure Block in SAVE mode directly underneath the room
            level.setBlock(sbPos, Blocks.STRUCTURE_BLOCK.defaultBlockState(), 3);
            if (level.getBlockEntity(sbPos) instanceof StructureBlockEntity sbe) {
                sbe.setMode(StructureMode.SAVE);
                sbe.setStructureName(id);
                sbe.setStructurePos(new BlockPos(0, 1, 0));
                sbe.setStructureSize(size);
                sbe.setIgnoreEntities(false);
                sbe.setShowBoundingBox(true);
                sbe.setShowAir(false);
                sbe.setChanged();
                level.sendBlockUpdated(sbPos, sbe.getBlockState(), sbe.getBlockState(), 3);
            }

            // 3. Place the structure at structPos (Y = 61)
            StructurePlaceSettings placeSettings = new StructurePlaceSettings()
                .setIgnoreEntities(false);
            template.placeInWorld(level, structPos, structPos, placeSettings, RandomSource.create(), 2);

            // 4. Place an Oak Sign in front of the Structure Block with the room name
            BlockPos signPos = sbPos.offset(0, 1, -1);
            level.setBlock(signPos, Blocks.OAK_SIGN.defaultBlockState(), 3);
            if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
                String path = id.getPath();
                String line1 = path.length() > 15 ? path.substring(0, 15) : path;
                String line2 = path.length() > 15 ? (path.length() > 30 ? path.substring(15, 30) : path.substring(15)) : "";
                SignText st = sign.getText(true)
                    .setMessage(0, Component.literal(line1))
                    .setMessage(1, Component.literal(line2));
                sign.setText(st, true);
                sign.setWaxed(true);
                sign.setChanged();
                level.sendBlockUpdated(signPos, sign.getBlockState(), sign.getBlockState(), 3);
            }

            // 5. Advance grid layout
            currentX += size.getX() + spacing;
            if (size.getZ() > maxRowZ) {
                maxRowZ = size.getZ();
            }
            placedCount++;
        }

        // Set day time and clear weather
        try {
            var server = level.getServer();
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "time set day");
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "weather clear");
        } catch (Exception ignored) {
        }

        if (player != null) {
            player.setGameMode(GameType.CREATIVE);
            if (!isReload) {
                player.teleportTo(0.5, 62.0, -3.5);
                player.setYRot(0f);
                player.setXRot(0f);
            }
            player.sendSystemMessage(Component.literal("§a[Rooms Dev] Successfully loaded and placed §e" + placedCount + "§a dungeon rooms with structure blocks!"));
        }

        return placedCount;
    }

    /**
     * Lists all rooms grouped by their type/theme, with clickable [TP] buttons
     * and room entries that teleport directly to each room.
     */
    public static void listRooms(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        Map<String, RoomInfo> roomMap = getOrComputeRooms(level);

        // Deduplicate into unique RoomInfo entries (since map has both full ID and path keys)
        Set<RoomInfo> uniqueRooms = new TreeSet<>(Comparator.comparing(r -> r.id().toString()));
        uniqueRooms.addAll(roomMap.values());

        if (uniqueRooms.isEmpty()) {
            source.sendFailure(Component.literal("§c[Rooms Dev] No dungeon rooms found!"));
            return;
        }

        // Group rooms by type
        Map<String, List<RoomInfo>> grouped = new TreeMap<>();
        for (RoomInfo r : uniqueRooms) {
            grouped.computeIfAbsent(r.type(), k -> new ArrayList<>()).add(r);
        }

        source.sendSuccess(() -> Component.literal("§6§l================== §e§lDungeon Rooms §7(" + uniqueRooms.size() + ") §6§l=================="), false);

        for (Map.Entry<String, List<RoomInfo>> entry : grouped.entrySet()) {
            String typeName = entry.getKey();
            List<RoomInfo> rooms = entry.getValue();

            source.sendSuccess(() -> Component.literal("§b§l[" + typeName + "] §7(" + rooms.size() + (rooms.size() == 1 ? " room" : " rooms") + ")"), false);

            for (RoomInfo info : rooms) {
                String cleanPath = info.id().getPath();
                String sizeStr = info.size().getX() + "x" + info.size().getY() + "x" + info.size().getZ();
                String tpCmd = "/rooms tp \"" + cleanPath + "\"";

                MutableComponent tpButton = Component.literal("[TP] ")
                    .withStyle(style -> style
                        .withColor(ChatFormatting.GREEN)
                        .withBold(true)
                        .withClickEvent(new ClickEvent.RunCommand(tpCmd))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("§aClick to teleport to §e" + cleanPath + "\n§7Position: " + (int)info.tpPos().x + ", " + (int)info.tpPos().y + ", " + (int)info.tpPos().z)))
                    );

                MutableComponent nameComp = Component.literal(cleanPath + " §7(" + sizeStr + ")")
                    .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withClickEvent(new ClickEvent.RunCommand(tpCmd))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("§aClick to teleport to §e" + cleanPath)))
                    );

                MutableComponent line = Component.literal("  ").append(tpButton).append(nameComp);
                source.sendSuccess(() -> line, false);
            }
        }

        source.sendSuccess(() -> Component.literal("§7Tip: Click §a[TP]§7 or room name to teleport directly in front of its structure block."), false);
    }

    /**
     * Teleports a player to the given room by full ID or path.
     */
    public static int teleportToRoom(CommandSourceStack source, String roomName) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("§c[Rooms Dev] Only players can teleport to rooms!"));
            return 0;
        }

        ServerLevel level = source.getLevel();
        Map<String, RoomInfo> roomMap = getOrComputeRooms(level);

        String clean = roomName.trim().replace("\"", "");
        RoomInfo info = roomMap.get(clean);

        if (info == null) {
            // Case-insensitive fallback search
            for (Map.Entry<String, RoomInfo> e : roomMap.entrySet()) {
                if (e.getKey().equalsIgnoreCase(clean)) {
                    info = e.getValue();
                    break;
                }
            }
        }

        if (info == null) {
            source.sendFailure(Component.literal("§c[Rooms Dev] Room not found: " + clean));
            return 0;
        }

        player.teleportTo(info.tpPos().x, info.tpPos().y, info.tpPos().z);
        player.setYRot(0f);
        player.setXRot(0f);

        final RoomInfo targetInfo = info;
        source.sendSuccess(() -> Component.literal("§a[Rooms Dev] Teleported to §e" + targetInfo.id().getPath() + " §7(Type: " + targetInfo.type() + ", Size: " + targetInfo.size().getX() + "x" + targetInfo.size().getY() + "x" + targetInfo.size().getZ() + ")"), false);
        return 1;
    }

    /**
     * Returns known room names for command autocompletion.
     */
    public static List<String> getKnownRoomNames(ServerLevel level) {
        Map<String, RoomInfo> roomMap = getOrComputeRooms(level);
        Set<String> names = new TreeSet<>();
        for (RoomInfo info : roomMap.values()) {
            names.add(info.id().getPath());
        }
        return new ArrayList<>(names);
    }
}
