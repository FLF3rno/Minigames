package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RemoveRespawningPlayerProcedure {
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
		execute(event, event.getEntity().level());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			MinigamesModVariables.MapVariables.get(world).respawningPlayers = MinigamesModVariables.MapVariables.get(world).respawningPlayers - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}