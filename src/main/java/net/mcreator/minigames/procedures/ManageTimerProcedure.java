package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ManageTimerProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).displayTimer == true) {
			if (MinigamesModVariables.MapVariables.get(world).achievement != 1) {
				MinigamesModVariables.MapVariables.get(world).gameTick = MinigamesModVariables.MapVariables.get(world).gameTick + 1;
				if (MinigamesModVariables.MapVariables.get(world).gameTick >= 60) {
					MinigamesModVariables.MapVariables.get(world).gameSeconds = MinigamesModVariables.MapVariables.get(world).gameSeconds + 1;
					MinigamesModVariables.MapVariables.get(world).gameTick = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).gameSeconds >= 60) {
					MinigamesModVariables.MapVariables.get(world).gameMinutes = MinigamesModVariables.MapVariables.get(world).gameMinutes + 1;
					MinigamesModVariables.MapVariables.get(world).gameSeconds = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).gameMinutes >= 60) {
					MinigamesModVariables.MapVariables.get(world).gameHours = MinigamesModVariables.MapVariables.get(world).gameHours + 1;
					MinigamesModVariables.MapVariables.get(world).gameMinutes = 0;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModVariables.MapVariables.get(world).gameTick == 0) {
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
		}
	}
}
