package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class ConquerTopLayerWorldProcedure {
	private static final ResourceKey<net.minecraft.world.level.Level> SPLEEF_DIMENSION = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:spleef_dimension"));
	private static final int COUNTDOWN_START = 1;
	private static final int COUNTDOWN_SOUND_1 = 280;
	private static final int COUNTDOWN_SOUND_2 = 320;
	private static final int COUNTDOWN_SOUND_3 = 360;
	private static final int COUNTDOWN_COMPLETE = 400;
	private static final int RESET_COOLDOWN_TICKS = 20;
	private static final int CLEAR_MIN = -20;
	private static final int CLEAR_MAX = 20;
	private static final int BASE_Y = 100;

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!(world instanceof ServerLevel level)) {
			return;
		}
		if (!level.dimension().equals(SPLEEF_DIMENSION)) {
			return;
		}

		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(level);
		if (!mapVariables.playingSpleef || mapVariables.layersRemainingSpleef <= 1) {
			return;
		}

		double topLayerY = getLayerY(mapVariables.layersRemainingSpleef, mapVariables.gapBetweenLayersSpleef);
		double belowLayerMinY = getLayerY(mapVariables.layersRemainingSpleef - 1, mapVariables.gapBetweenLayersSpleef);
		double belowLayerMaxY = topLayerY - 1;

		List<ServerPlayer> playersOnTopLayer = getPlayersAboveY(level, topLayerY);
		int playersBelowTopLayer = getPlayersInRange(level, belowLayerMinY, belowLayerMaxY).size();

		int previousCountdown = (int) mapVariables.layerCountdownSpleef;
		int nextCountdown = previousCountdown;
		int previousCooldown = (int) mapVariables.layerConquestCooldownSpleef;
		int nextCooldown = previousCooldown;

		if (nextCooldown > 0) {
			nextCooldown--;
		}

		if (nextCooldown > 0) {
			nextCountdown = 0;
		} else if (playersOnTopLayer.size() <= 1) {
			nextCountdown = previousCountdown + 1;
			if (playersBelowTopLayer == 0) {
				nextCountdown = COUNTDOWN_COMPLETE;
			}
		} else {
			nextCountdown = 0;
			nextCooldown = RESET_COOLDOWN_TICKS;
		}

		if (nextCountdown != previousCountdown || nextCooldown != previousCooldown) {
			mapVariables.layerCountdownSpleef = nextCountdown;
			mapVariables.layerConquestCooldownSpleef = nextCooldown;
			mapVariables.markSyncDirty();
		}

		if (nextCountdown == COUNTDOWN_START && previousCountdown < COUNTDOWN_START) {
			playCountdownSound(level);
			if (playersOnTopLayer.size() == 1) {
				ConquerTopLayerProcedure.announcePotentialWinner(playersOnTopLayer.get(0));
			}
		}

		if (previousCountdown < COUNTDOWN_SOUND_1 && nextCountdown >= COUNTDOWN_SOUND_1) {
			playCountdownSound(level);
		}
		if (previousCountdown < COUNTDOWN_SOUND_2 && nextCountdown >= COUNTDOWN_SOUND_2) {
			playCountdownSound(level);
		}
		if (previousCountdown < COUNTDOWN_SOUND_3 && nextCountdown >= COUNTDOWN_SOUND_3) {
			playCountdownSound(level);
		}

		if (previousCountdown < COUNTDOWN_COMPLETE && nextCountdown >= COUNTDOWN_COMPLETE) {
			if (playersOnTopLayer.size() == 1) {
				ConquerTopLayerProcedure.awardLayerWin(level, playersOnTopLayer.get(0));
			}

			clearTopLayer(level, topLayerY);
			mapVariables.layerCountdownSpleef = 0;
			mapVariables.layerConquestCooldownSpleef = RESET_COOLDOWN_TICKS;
			mapVariables.layersRemainingSpleef = mapVariables.layersRemainingSpleef - 1;
			mapVariables.markSyncDirty();
		}
	}

	private static double getLayerY(double layerIndex, double gap) {
		return BASE_Y + layerIndex * gap;
	}

	private static List<ServerPlayer> getPlayersInRange(ServerLevel level, double minY, double maxY) {
		List<ServerPlayer> players = new ArrayList<>();
		for (Player player : level.players()) {
			if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && serverPlayer.getY() >= minY && serverPlayer.getY() <= maxY
					&& isInsideArena(serverPlayer)) {
				players.add(serverPlayer);
			}
		}
		return players;
	}

	private static List<ServerPlayer> getPlayersAboveY(ServerLevel level, double minY) {
		List<ServerPlayer> players = new ArrayList<>();
		for (Player player : level.players()) {
			if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && serverPlayer.getY() >= minY && isInsideArena(serverPlayer)) {
				players.add(serverPlayer);
			}
		}
		return players;
	}

	private static boolean isInsideArena(ServerPlayer player) {
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(player.level()).spleefMapMiddleX;
		return player.getX() >= arenaCenter.x() - 100 && player.getX() <= arenaCenter.x() + 100 && player.getZ() >= arenaCenter.z() - 100 && player.getZ() <= arenaCenter.z() + 100;
	}

	private static void playCountdownSound(ServerLevel level) {
		level.getServer().getCommands().performPrefixedCommand(getSource(level), "/execute as @a at @s run playsound minigames:menu_switch master @s ~ ~ ~ 1 1");
	}

	private static void clearTopLayer(ServerLevel level, double y) {
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(level).spleefMapMiddleX;
		int fillY = (int) Math.floor(y);
		level.getServer().getCommands().performPrefixedCommand(
				getSource(level),
				"execute in minigames:spleef_dimension run fill " + ((int) arenaCenter.x() + CLEAR_MIN) + " " + fillY + " " + ((int) arenaCenter.z() + CLEAR_MIN) + " " + ((int) arenaCenter.x() + CLEAR_MAX) + " 140 "
						+ ((int) arenaCenter.z() + CLEAR_MAX) + " air");
	}

	private static CommandSourceStack getSource(ServerLevel level) {
		return new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, level, 4, "", net.minecraft.network.chat.Component.literal(""), level.getServer(), null).withSuppressedOutput();
	}
}
