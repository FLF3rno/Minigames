package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

@EventBusSubscriber
public class FirstTimeJoinProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.MAX_BLOCK_MODIFICATIONS, 999999999, world.getServer());
	}
}