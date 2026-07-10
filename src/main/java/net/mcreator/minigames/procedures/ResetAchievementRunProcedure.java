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

public class ResetAchievementRunProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/worldborder set 50000000");
		if (MinigamesModVariables.MapVariables.get(world).randomizeSpawn == false) {
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
			MinigamesMod.queueServerWork(20, () -> {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/tp @a @a[team=spread,limit=1]");
			});
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamemode survival @a");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/advancement revoke @a everything");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/gamerule locatorBar false");
		MinigamesModVariables.MapVariables.get(world).p1state = 0;
		MinigamesModVariables.MapVariables.get(world).p2state = 0;
		MinigamesModVariables.MapVariables.get(world).p3state = 0;
		MinigamesModVariables.MapVariables.get(world).p4state = 0;
		MinigamesModVariables.MapVariables.get(world).p5state = 0;
		MinigamesModVariables.MapVariables.get(world).p6state = 0;
		MinigamesModVariables.MapVariables.get(world).displayTimer = false;
		MinigamesModVariables.MapVariables.get(world).gameTick = 0;
		MinigamesModVariables.MapVariables.get(world).gameSeconds = 0;
		MinigamesModVariables.MapVariables.get(world).gameMinutes = 0;
		MinigamesModVariables.MapVariables.get(world).gameHours = 0;
		MinigamesModVariables.MapVariables.get(world).pvpstate = -1;
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			NameColorApplyProcedure.applyColor(world, entityiterator);
		}
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		RemoveWinAnimationProcedure.execute(world);
	}
}




