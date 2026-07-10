package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.Event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

public class ConquerTopLayerProcedure {
	private ConquerTopLayerProcedure() {
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (!(entity instanceof ServerPlayer player)) {
			return;
		}
		if (!MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			return;
		}
		if (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef <= 1) {
			return;
		}
	}

	public static void announcePotentialWinner(ServerPlayer player) {
		if (player.level().getServer() == null) {
			return;
		}
		player.level().getServer().getPlayerList().broadcastSystemMessage(
				Component.literal(player.getName().getString()).withStyle(style -> style.withBold(true).withColor(net.minecraft.ChatFormatting.GOLD))
						.append(Component.literal(" is about to win the layer!").withStyle(style -> style.withColor(net.minecraft.ChatFormatting.GREEN))),
				false);
	}

	public static void awardLayerWin(LevelAccessor world, ServerPlayer player) {
		if (player.level().getServer() == null) {
			return;
		}
		player.level().getServer().getPlayerList().broadcastSystemMessage(
				Component.literal(player.getName().getString()).withStyle(style -> style.withBold(true).withColor(net.minecraft.ChatFormatting.GOLD))
						.append(Component.literal(" won the layer!").withStyle(style -> style.withColor(net.minecraft.ChatFormatting.LIGHT_PURPLE))),
				false);
		{
			MinigamesModVariables.PlayerVariables variables = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
			variables.snowballCountSpleef = player.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 99;
			variables.markSyncDirty();
		}
		SpleefPowerupProcedure.execute(world, player);
	}
}



