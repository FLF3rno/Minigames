package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.MinigamesMod;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class CommandGridProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "fill 30 300 30 1 300 1 air");
		MinigamesMod.queueServerWork(1, () -> {
			SpawnGridProcedure.execute(world, x, y, z, DoubleArgumentType.getDouble(arguments, "loot"), DoubleArgumentType.getDouble(arguments, "maximumRooms"), DoubleArgumentType.getDouble(arguments, "miniboss"),
					DoubleArgumentType.getDouble(arguments, "minimumRooms"), DoubleArgumentType.getDouble(arguments, "roomX"), DoubleArgumentType.getDouble(arguments, "roomZ"), DoubleArgumentType.getDouble(arguments, "secret"));
		});
	}
}