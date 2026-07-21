package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
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
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (TimerSecondsProcedure.execute(entityiterator) == MinigamesModVariables.MapVariables.get(world).WhenPVPActive && entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerTick == 0) {
					StartPVPProcedure.execute(world);
				}
				break;
			}
			AnimationTick = MinigamesModVariables.MapVariables.get(world).pvpAnimationTick;
			pvpState = MinigamesModVariables.MapVariables.get(world).pvpstate;
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationStart == true) {
				AnimationTick = AnimationTick + 1;
				if (AnimationTick == 1) {
					pvpState = 0;
				}
				if (AnimationTick == 3) {
					pvpState = 6;
				}
				if (AnimationTick == 5) {
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _switchworld2 = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (_switchworld2 != null) {
							worldSwitch2(world);
						}
					}
					pvpState = 5;
				}
				if (AnimationTick == 7) {
					pvpState = 4;
				}
				if (AnimationTick == 9) {
					pvpState = 3;
				}
				if (AnimationTick == 11) {
					pvpState = 2;
				}
				if (AnimationTick == 30) {
					pvpState = 1;
				}
				if (AnimationTick == 32) {
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _switchworld4 = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (_switchworld4 != null) {
							worldSwitch4(world);
						}
					}
					pvpState = 0;
				}
				if (AnimationTick == 86) {
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _switchworld7 = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (_switchworld7 != null) {
							worldSwitch7(world);
						}
					}
					pvpState = 7;
				}
				if (AnimationTick == 88) {
					pvpState = 8;
				}
				if (AnimationTick == 90) {
					pvpState = 9;
				}
				if (AnimationTick == 300) {
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _switchworld9 = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (_switchworld9 != null) {
							worldSwitch9(world);
						}
					}
					pvpState = 8;
				}
				if (AnimationTick == 302) {
					pvpState = 7;
				}
				if (AnimationTick == 304) {
					pvpState = 0;
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _switchworld11 = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (_switchworld11 != null) {
							worldSwitch11(world);
						}
					}
				}
				if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 340) {
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

	private static void worldSwitch2(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
	}

	private static void worldSwitch4(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:pvp_swordshit player @a ~ ~ ~ 10000000");
	}

	private static void worldSwitch7(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:ui.toast.in player @a ~ ~ ~ 10000000 1");
	}

	private static void worldSwitch9(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:ui.toast.out player @a ~ ~ ~ 10000000 5");
	}

	private static void worldSwitch11(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:pvp_swordshit player @a ~ ~ ~ 10000000");
	}
}