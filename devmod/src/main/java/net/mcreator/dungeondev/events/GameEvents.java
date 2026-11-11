package net.mcreator.dungeondev.events;

import net.mcreator.dungeondev.client.DevWorldHelper;
import net.mcreator.dungeondev.world.DungeonRoomManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class GameEvents {

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.level() instanceof ServerLevel level) {
                String levelName = level.getServer().getWorldData().getLevelName();

                if (DevWorldHelper.WORLD_NAME.equalsIgnoreCase(levelName) || DevWorldHelper.DISPLAY_NAME.equalsIgnoreCase(levelName)) {
                    boolean alreadyGenerated = level.getBlockState(new BlockPos(0, 60, 0)).is(Blocks.STRUCTURE_BLOCK);
                    if (!alreadyGenerated) {
                        // Automatically generate rooms on initial world entry
                        DungeonRoomManager.generateRooms(level, player, false);
                    } else {
                        player.setGameMode(GameType.CREATIVE);
                        player.sendSystemMessage(Component.literal("§6[Rooms Dev] Welcome back! Run §e/rooms reload§6 to refresh newly added structures."));
                    }
                }
            }
        }
    }
}
