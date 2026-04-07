package net.mcreator.minigames.procedures;

import org.checkerframework.checker.units.qual.min;

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
public class AdvanceWinAnimationProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		String sec = "";
		String min = "";
		String hour = "";
		if (!(world instanceof ServerLevel))
			return;
		if (MinigamesModVariables.MapVariables.get(world).winAnimationStart == true) {
			MinigamesModVariables.MapVariables.get(world).winAnimationTick = MinigamesModVariables.MapVariables.get(world).winAnimationTick + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode == false) {
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 1) {
				MinigamesModVariables.winAnimation = 0;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 2) {
				MinigamesModVariables.winAnimation = 1;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 3) {
				MinigamesModVariables.winAnimation = 2;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 4) {
				MinigamesModVariables.winAnimation = 3;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 5) {
				MinigamesModVariables.winAnimation = 4;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 6) {
				MinigamesModVariables.winAnimation = 5;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 7) {
				MinigamesModVariables.winAnimation = 6;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 86) {
				MinigamesModVariables.winAnimation = 7;
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:teameliminated player @a ~ ~ ~ 1000000 1");
					}
					world = _worldorig;
				}
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick >= 483) {
				MinigamesModVariables.winAnimation = -1;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/clear @a minigames:crown_helmet_helmet");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"give @a minigames:game_compass");
				MinigamesModVariables.MapVariables.get(world).winAnimationStart = false;
				MinigamesModVariables.MapVariables.get(world).winAnimationTick = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		} else if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode == true) {
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 1) {
				MinigamesModVariables.winAnimation = 7;
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:teameliminated player @a ~ ~ ~ 1000000 1");
					}
					world = _worldorig;
				}
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick >= 406) {
				MinigamesModVariables.winAnimation = -1;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/clear @a minigames:crown_helmet_helmet");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"give @a minigames:game_compass");
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = false;
				MinigamesModVariables.MapVariables.get(world).hunteraWinAnimation = false;
				MinigamesModVariables.MapVariables.get(world).winAnimationStart = false;
				MinigamesModVariables.MapVariables.get(world).winAnimationTick = -1;
				MinigamesModVariables.MapVariables.get(world).hunterAchievement = "";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
		if (MinigamesModVariables.MapVariables.get(world).winAnimationState != MinigamesModVariables.winAnimation) {
			MinigamesModVariables.MapVariables.get(world).winAnimationState = MinigamesModVariables.winAnimation;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}
