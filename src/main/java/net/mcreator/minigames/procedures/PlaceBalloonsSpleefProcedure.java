package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlaceBalloonsSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double nightTime = 0;
		MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX = new Vec3(0, 100, 0);
		MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef = 6;
		MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef = 5;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute in minigames:spleef_dimension run place template minigames:spleef_balloons_arena " + (21 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()) + " 99 -16"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute in minigames:spleef_dimension run spreadplayers " + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() + " 0 3 15 false @a"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute as @a at @s run tp @s ~ ~ ~ facing " + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x() + " 124 0"));
		nightTime = Mth.nextInt(RandomSource.create(), 1, 3);
		if (nightTime == 3) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"time set midnight");
		}
		MinigamesModVariables.MapVariables.get(world).currentMapSpleef = "balloons";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}