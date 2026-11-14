package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StopSpleefProcedure {
	public static void execute(LevelAccessor world) {
		String firstUuid = MinigamesModVariables.firstSpleef != null ? MinigamesModVariables.firstSpleef.getStringUUID() : "";
		String secondUuid = MinigamesModVariables.secondSpleef != null ? MinigamesModVariables.secondSpleef.getStringUUID() : "";
		String thirdUuid = MinigamesModVariables.thirdSpleef != null ? MinigamesModVariables.thirdSpleef.getStringUUID() : "";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level) {
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect clear @a night_vision");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamerule snowAccumulationHeight 1");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamerule doDaylightCycle true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/time set noon");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamerule doWeatherCycle true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamerule doMobSpawning true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"gamemode adventure @a");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect give @a minecraft:weakness infinite 100 true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect give @a minecraft:weakness infinite 100 true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect give @a minecraft:resistance infinite 9 true");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/weather clear");
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run tp @a -945 101 -984 facing -946 101 -984");
			}
			
		MinigamesMod.queueServerWork(10, () -> {
			if (world instanceof ServerLevel _level) {
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute in minigames:spleef_dimension run forceload add -950 -0 -925 32");
			}
			if (!firstUuid.isEmpty())
				ChangePodiumTextureProcedure.execute(world, 1, firstUuid);
			if (!secondUuid.isEmpty())
				ChangePodiumTextureProcedure.execute(world, 2, secondUuid);
			if (!thirdUuid.isEmpty())
				ChangePodiumTextureProcedure.execute(world, 3, thirdUuid);
			MinigamesMod.queueServerWork(20, () -> {
				if (world instanceof ServerLevel _level) {
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/execute in minigames:spleef_dimension run forceload remove -950 -0 -925 32");
				}
			});
		});
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "clear @a");
		for (Entity entityiterator : new ArrayList<>(world.getServer().getPlayerList().getPlayers())) {
			GrantGameCompassProcedure.execute(world, entityiterator);
		}
		MinigamesModVariables.MapVariables.get(world).playingSpleef = false;
		MinigamesModVariables.MapVariables.get(world).sky = "normal";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}





