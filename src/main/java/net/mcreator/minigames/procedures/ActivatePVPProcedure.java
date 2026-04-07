package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

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
		double animationSpeed1 = 0;
		double animationSpeed2 = 0;
		if (MinigamesModVariables.MapVariables.get(world).achievement != -1) {
			if (MinigamesModVariables.MapVariables.get(world).gameTick == 0 && MinigamesModVariables.MapVariables.get(world).gameSeconds == 0 && MinigamesModVariables.MapVariables.get(world).gameMinutes == 5
					&& MinigamesModVariables.MapVariables.get(world).gameHours == 0 && MinigamesModVariables.MapVariables.get(world).achievementHunterMode == false) {
				StartPVPProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationStart == true) {
				MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = MinigamesModVariables.MapVariables.get(world).pvpAnimationTick + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 1) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 3) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 6;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 5) {
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
					}
					world = _worldorig;
				}
				MinigamesModVariables.MapVariables.get(world).pvpstate = 5;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 7) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 4;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 9) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 3;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 11) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 2;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 30) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 32) {
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:pvp_swordshit player @a ~ ~ ~ 10000000");
					}
					world = _worldorig;
				}
				MinigamesModVariables.MapVariables.get(world).pvpstate = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 86) {
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:pvp_swind player @a ~ ~ ~ 100000000 1");
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minecraft:ui.toast.in player @a ~ ~ ~ 10000000 1");
					}
					world = _worldorig;
				}
				MinigamesModVariables.MapVariables.get(world).pvpstate = 7;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 88) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 8;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 90) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 9;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 300) {
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minecraft:ui.toast.out player @a ~ ~ ~ 10000000 5");
					}
					world = _worldorig;
				}
				MinigamesModVariables.MapVariables.get(world).pvpstate = 8;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 302) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 7;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 304) {
				MinigamesModVariables.MapVariables.get(world).pvpstate = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:pvp_swordshit player @a ~ ~ ~ 10000000");
					}
					world = _worldorig;
				}
			}
			if (MinigamesModVariables.MapVariables.get(world).pvpAnimationTick == 340) {
				MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = 0;
				MinigamesModVariables.MapVariables.get(world).pvpAnimationStart = false;
				MinigamesModVariables.MapVariables.get(world).pvpstate = -1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}
}