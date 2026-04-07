package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HunterAnimationAchievementProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!world.isClientSide()) {
			if (MinigamesModVariables.MapVariables.get(world).animateHunter == true) {
				MinigamesModVariables.MapVariables.get(world).animateHunterState = MinigamesModVariables.MapVariables.get(world).animateHunterState + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (MinigamesModVariables.MapVariables.get(world).achievmentType == 1) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = Mth.nextInt(RandomSource.create(), 1, (int) MinigamesModVariables.MapVariables.get(world).players);
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 3) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 5) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 7) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 11) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 16) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 24) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 30) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 38) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 50) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 66) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 82) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 102) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 130) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 159) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).animateHunterState == 160) {
					MinigamesModVariables.MapVariables.get(world).animateHunter = false;
					MinigamesModVariables.MapVariables.get(world).animateHunterState = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					if (world instanceof ServerLevel _origLevel) {
						LevelAccessor _worldorig = world;
						world = _origLevel.getServer().getLevel(Level.OVERWORLD);
						if (world != null) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"playsound block.note_block.pling player @a ~ ~ ~ 1000000000000 2");
						}
						world = _worldorig;
					}
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation > MinigamesModVariables.MapVariables.get(world).players) {
					MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation = 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 1) {
					MinigamesModVariables.MapVariables.get(world).p1state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p1state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 2) {
					MinigamesModVariables.MapVariables.get(world).p2state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p2state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 3) {
					MinigamesModVariables.MapVariables.get(world).p3state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p3state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 4) {
					MinigamesModVariables.MapVariables.get(world).p4state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p4state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 5) {
					MinigamesModVariables.MapVariables.get(world).p5state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p5state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation == 6) {
					MinigamesModVariables.MapVariables.get(world).p6state = 3;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).p6state = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
		}
		if (MinigamesModVariables.MapVariables.get(world).animateHunter == true) {
			if (MinigamesModVariables.MapVariables.get(world).achievmentType == 1) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"execute as @a at @s run playsound minigames:rollaudioclean master @s ~ ~ ~ 1 1");
			}
		}
	}
}