package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class ActivatePVPProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		double AnimationTick = 0;
		double pvpState = 0;
		double speed = 0;
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (TimerSecondsProcedure.execute(entityiterator) == MinigamesModVariables.MapVariables.get(world).WhenPVPActive && entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerTick == 0) {
					StartPVPProcedure.execute(world);
				}
				break;
			}
			AnimationTick = MinigamesModVariables.MapVariables.get(world).pvpAnimationTick;
			pvpState = MinigamesModVariables.MapVariables.get(world).pvpstate;
			speed = 2;
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationStart == true) {
				AnimationTick = AnimationTick + 1;
				if (AnimationTick == Math.round(1 * speed)) {
					pvpState = 0;
				}
				if (AnimationTick == Math.round(3 * speed)) {
					pvpState = 6;
				}
				if (AnimationTick == Math.round(5 * speed)) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
					pvpState = 5;
				}
				if (AnimationTick == Math.round(7 * speed)) {
					pvpState = 4;
				}
				if (AnimationTick == Math.round(9 * speed)) {
					pvpState = 3;
				}
				if (AnimationTick == Math.round(11 * speed)) {
					pvpState = 2;
				}
				if (AnimationTick == Math.round(30 * speed)) {
					pvpState = 1;
				}
				if (AnimationTick == Math.round(32 * speed)) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minigames:pvp_swordshit player @a ~ ~ ~ 10000000");
					pvpState = 0;
				}
				if (AnimationTick == Math.round(86 * speed)) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minecraft:ui.toast.in player @a ~ ~ ~ 10000000 1");
					pvpState = 7;
				}
				if (AnimationTick == Math.round(88 * speed)) {
					pvpState = 8;
				}
				if (AnimationTick == Math.round(90 * speed)) {
					pvpState = 9;
				}
				if (AnimationTick == Math.round(300 * speed)) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minecraft:ui.toast.out player @a ~ ~ ~ 10000000 5");
					pvpState = 8;
				}
				if (AnimationTick == Math.round(302 * speed)) {
					pvpState = 7;
				}
				if (AnimationTick == Math.round(304 * speed)) {
					pvpState = 0;
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(
								new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/execute as @a at @s run playsound minigames:pvp_swordshit player @s ~ ~ ~ 1");
				}
				if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == Math.round(340 * speed)) {
					AnimationTick = 0;
					MinigamesModVariables.MapVariables.get(world).pvpAnimationStart = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					pvpState = -1;
				}
				MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = AnimationTick;
				MinigamesModVariables.MapVariables.get(world).pvpstate = pvpState;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}
}