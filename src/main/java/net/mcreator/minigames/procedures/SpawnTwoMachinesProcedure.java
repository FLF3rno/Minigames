package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class SpawnTwoMachinesProcedure {
	public static void execute(LevelAccessor world) {
		int first = Mth.nextInt(RandomSource.create(), 1, 4);

int second;
do {
    second = Mth.nextInt(RandomSource.create(), 1, 4);
} while (second == first);

if (world instanceof ServerLevel level) {

    String firstCommand = switch (first) {
        case 1 -> "/execute as @e[type=minigames:flavio_trapdoor_2] at @s run summon minigames:flavio_tesla_coil ~ ~-3.1 ~";
        case 2 -> "/execute as @e[type=minigames:flavio_trapdoor_2] at @s run summon minigames:flavio_antenna ~ ~-3.1 ~";
        case 3 -> "/execute as @e[type=minigames:flavio_trapdoor_2] at @s run summon minigames:flavio_clock_cannon ~ ~-3.1 ~";
        case 4 -> "/execute as @e[type=minigames:flavio_trapdoor_2] at @s run summon minigames:flavio_sweeper ~ ~-3.1 ~";
        default -> "";
    };

    String secondCommand = switch (second) {
        case 1 -> "/execute as @e[type=minigames:flavio_trapdoor_3] at @s run summon minigames:flavio_tesla_coil ~ ~-3.1 ~";
        case 2 -> "/execute as @e[type=minigames:flavio_trapdoor_3] at @s run summon minigames:flavio_antenna ~ ~-3.1 ~";
        case 3 -> "/execute as @e[type=minigames:flavio_trapdoor_3] at @s run summon minigames:flavio_clock_cannon ~ ~-3.1 ~";
        case 4 -> "/execute as @e[type=minigames:flavio_trapdoor_3] at @s run summon minigames:flavio_sweeper ~ ~-3.1 ~";
        default -> "";
    };

    CommandSourceStack source = new CommandSourceStack(
        CommandSource.NULL,
        new Vec3(1, 1, 1),
        Vec2.ZERO,
        level,
        LevelBasedPermissionSet.OWNER,
        "",
        Component.literal(""),
        level.getServer(),
        null
    ).withSuppressedOutput();

    level.getServer().getCommands().performPrefixedCommand(source, firstCommand);
    level.getServer().getCommands().performPrefixedCommand(source, secondCommand);
}
	}
}