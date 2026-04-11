package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlaceSolarSystemSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double nightTime = 0;
		MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX = new Vec3(1000, 100, 0);
		MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef = 5;
		MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef = 5;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run forceload add 970 -30 1030 30");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run place template minigames:spleef_sun 985 100 -15");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run spreadplayers 1000 0 3 15 false @a");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute as @a at @s run tp @s ~ ~ ~ facing 1000 120 0");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run forceload remove 970 -30 1030 30");
		MinigamesModVariables.MapVariables.get(world).sky = "space";
		MinigamesModVariables.MapVariables.get(world).currentMapSpleef = "solar system";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}