package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RollTypeProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		String sound = "";
		if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer < 295) {
			MinigamesModVariables.MapVariables.get(world).playersReady = 0;
			MinigamesModVariables.MapVariables.get(world).achivementTypeTimer = MinigamesModVariables.MapVariables.get(world).achivementTypeTimer + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 1) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"playsound minigames:rollaudio player @a ~ ~ ~ 1000000 1");
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 3) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 5) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 7) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 11) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 16) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 24) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 30) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 38) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 50) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 66) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 82) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 102) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 130) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = MinigamesModVariables.MapVariables.get(world).achievmentType + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 160) {
				MinigamesModVariables.MapVariables.get(world).achievmentType = Mth.nextInt(RandomSource.create(), 1, 4);
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"playsound block.note_block.pling player @a ~ ~ ~ 1000000000000 2");
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 240) {
				MinigamesModVariables.MapVariables.get(world).overlayAnimation1 = MinigamesModVariables.MapVariables.get(world).overlayAnimation1 + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 242) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 246) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 250) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 254) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 258) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 262) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 266) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 270) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 274) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 278) {
				BreakAnimationProcedure.execute(world);
			}
			if (MinigamesModVariables.MapVariables.get(world).achivementTypeTimer == 282) {
				ChooseAchievementProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).overlayAnimation1 = 0;
				MinigamesModVariables.MapVariables.get(world).openGameGUI = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (MinigamesModVariables.MapVariables.get(world).randomHunterAchievement == true) {
					MinigamesModVariables.MapVariables.get(world).animateHunter = true;
					MinigamesModVariables.MapVariables.get(world).animateHunterState = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
		}
		if (MinigamesModVariables.MapVariables.get(world).achievmentType == 4) {
			MinigamesModVariables.MapVariables.get(world).achievmentType = 0;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}