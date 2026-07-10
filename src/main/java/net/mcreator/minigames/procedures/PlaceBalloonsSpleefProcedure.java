package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlaceBalloonsSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX = new Vec3(0, 100, 0);
		MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef = 6;
		MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef = 5;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute in minigames:spleef_dimension run forceload add " + new java.text.DecimalFormat("##").format(30 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()) + " "
							+ new java.text.DecimalFormat("##").format(30 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z()) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() - 30) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z() - 30)));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute in minigames:spleef_dimension run fill " + new java.text.DecimalFormat("##").format(22 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()) + " "
							+ new java.text.DecimalFormat("##").format(100 + MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef) + " "
							+ new java.text.DecimalFormat("##").format(22 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z()) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() - 22) + " 100 "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z() - 22) + " air"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"execute in minigames:spleef_dimension run place template minigames:spleef_balloons_arena -21 100 -14");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute in minigames:spleef_dimension run spreadplayers " + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z()) + " 3 15 false @a"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute as @a at @s run tp @s ~ ~ ~ facing " + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() + " "
							+ (100 + MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef) + " "
							+ MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z()));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute in minigames:spleef_dimension run forceload remove " + new java.text.DecimalFormat("##").format(30 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()) + " "
							+ new java.text.DecimalFormat("##").format(30 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z()) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() - 30) + " "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.z() - 30)));
		MinigamesModVariables.MapVariables.get(world).currentMapSpleef = "balloons";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (Math.random() < 0.2) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "time set midnight");
		} else {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "time set day");
		}
	}
}