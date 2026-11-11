package net.mcreator.dungeondev.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.mcreator.dungeondev.world.DungeonRoomManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class RoomCommands {

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("rooms")
                .then(Commands.literal("reload")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        ServerLevel level = source.getLevel();
                        ServerPlayer player = source.getPlayer();
                        int count = DungeonRoomManager.generateRooms(level, player, true);
                        source.sendSuccess(() -> Component.literal("§a[Rooms Dev] Reloaded §e" + count + "§a dungeon rooms!"), true);
                        return count;
                    })
                )
                .then(Commands.literal("list")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        DungeonRoomManager.listRooms(source);
                        return 1;
                    })
                )
                .then(Commands.literal("tp")
                    .then(Commands.argument("room", StringArgumentType.greedyString())
                        .suggests((ctx, builder) -> {
                            ServerLevel level = ctx.getSource().getLevel();
                            return SharedSuggestionProvider.suggest(DungeonRoomManager.getKnownRoomNames(level), builder);
                        })
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            String room = StringArgumentType.getString(ctx, "room");
                            return DungeonRoomManager.teleportToRoom(source, room);
                        })
                    )
                )
        );
    }
}
