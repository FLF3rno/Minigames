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

public class SpawnSingleMachineProcedure {
	public static void execute(LevelAccessor world) {
		double rng = 0;
		rng = Mth.nextInt(RandomSource.create(), 1, 4);
		if (rng == 1) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(1, 1, 1), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute as @e[type=minigames:flavio_trapdoor] at @s run summon minigames:flavio_tesla_coil ~ ~-3.1 ~");
		} else if (rng == 2) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(1, 1, 1), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute as @e[type=minigames:flavio_trapdoor] at @s run summon minigames:flavio_antenna ~ ~-3.1 ~");
		} else if (rng == 3) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(1, 1, 1), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute as @e[type=minigames:flavio_trapdoor] at @s run summon minigames:flavio_clock_cannon ~ ~-3.1 ~");
		} else if (rng == 4) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(1, 1, 1), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute as @e[type=minigames:flavio_trapdoor] at @s run summon minigames:flavio_sweeper ~ ~-3.1 ~");
		}
	}
}