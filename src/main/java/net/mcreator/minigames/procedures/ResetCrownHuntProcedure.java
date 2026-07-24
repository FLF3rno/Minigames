package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class ResetCrownHuntProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minecraft:overworld run spreadplayers ~ ~ 0 10 under 200 true @a");
		MinigamesMod.queueServerWork(20, () -> {
			MinigamesModVariables.MapVariables.get(world).gameHours = 1;
			MinigamesModVariables.MapVariables.get(world).CrownHuntInGame = false;
			MinigamesModVariables.MapVariables.get(world).minimap = false;
			MinigamesModVariables.MapVariables.get(world).waypoints = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/kill @e[type=armor_stand,nbt={equipment:{head:{id:\"minigames:crown_helmet_helmet\",count:1}}}]");
			MinigamesModVariables.MapVariables.get(world).removeEffects = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/effect give @a minigames:immobilized 10 0 true");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/advancement revoke @a everything");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamerule locatorBar false");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamemode survival @a");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/worldborder set 50000000");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/time set noon");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/clear @a");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/xp set @a 0");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/xp set @a 0 levels");
			if (world instanceof Level _level)
				_level.getScoreboard().addPlayerTeam("spread");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/team join spread @r");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute in minecraft:overworld run spreadplayers ~ ~ 1000000 1000000 under 200 true @a[team=spread]");
		});
		MinigamesMod.queueServerWork(40, () -> {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/tp @a[team=!spread] @a[team=spread,limit=1]");
		});
		MinigamesMod.queueServerWork(50, () -> {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute at @a[team=spread,limit=1] run place template minigames:crown_castle ~-6 ~ ~-6");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute at @a[team=spread,limit=1] run worldborder center ~ ~");
			MinigamesModVariables.MapVariables.get(world).ShowTimer = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerSpeed = -1;
					_vars.markSyncDirty();
				}
			}
			MinigamesModVariables.MapVariables.get(world).applyCustomNameColor = true;
			for (Entity entityiterator : new java.util.ArrayList<>(world.players())) {
				NameColorApplyProcedure.applyColor(world, entityiterator);
			}
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			StartGameCrownHuntProcedure.execute(world, x, y, z);
		});
	}
}




