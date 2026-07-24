package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AchievementHunterAnimationProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		Entity player = null;
		if (world instanceof ServerLevel level) {
			if (level != level.getServer().getLevel(Level.OVERWORLD))
				return;
		}
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement && MinigamesModVariables.MapVariables.get(world).achievementHunterMode) {
			MinigamesModVariables.MapVariables.get(world).hunterAnimation = MinigamesModVariables.MapVariables.get(world).hunterAnimation + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world.getServer() == null || world.getServer().getPlayerList().getPlayers().isEmpty()) {
				return;
			}
			player = world.getServer().getPlayerList().getPlayers().get(RandomSource.create().nextInt(world.getServer().getPlayerList().getPlayers().size()));
			if (MinigamesModVariables.MapVariables.get(world).hunterAnimation % 15 == 0 && MinigamesModVariables.MapVariables.get(world).hunterAnimation < 105) {
				if (MinigamesModVariables.MapVariables.get(world).WinnerList.isEmpty()) {
					MinigamesModVariables.MapVariables.get(world).WinnerList.add((player.getStringUUID()));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					MinigamesModVariables.MapVariables.get(world).WinnerList.set(0, (player.getStringUUID()));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/execute as @a at @s run playsound minecraft:block.note_block.pling ui @s ~ ~ ~ 1 1.1");
			} else if (MinigamesModVariables.MapVariables.get(world).hunterAnimation == 105) {
				MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID = player.getStringUUID();
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				MinigamesModVariables.MapVariables.get(world).WinnerList.set(0, (player.getStringUUID()));
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (world instanceof Level _level)
					_level.getScoreboard().addPlayerTeam("hunted");
				{
					Entity _entityTeam = player;
					PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam("hunted");
					if (_pt != null) {
						if (_entityTeam instanceof Player _player)
							_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().name(), _pt);
						else
							_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
					}
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"team modify hunted prefix {\"text\":\"\\uE000---\",\"font\":\"minigames:icons\",\"color\":\"white\"}");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/execute as @a at @s run playsound minecraft:block.note_block.pling ui @s ~ ~ ~ 1 2");
			}
		}
	}
}