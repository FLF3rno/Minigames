package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class TimeOverCrownhuntProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame == true) {
			if (MinigamesModVariables.MapVariables.get(world).canGrabCrown == false) {
				if (MinigamesModVariables.MapVariables.get(world).gameHours == 0 && MinigamesModVariables.MapVariables.get(world).gameMinutes == 0 && MinigamesModVariables.MapVariables.get(world).gameSeconds == 0) {
					MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = false;
					MinigamesModVariables.MapVariables.get(world).ShowCrownTimer = false;
					MinigamesModVariables.MapVariables.get(world).inGracePeriod = false;
					MinigamesModVariables.MapVariables.get(world).gameHours = 1;
					MinigamesModVariables.MapVariables.get(world).gameMinutes = 1;
					MinigamesModVariables.MapVariables.get(world).gameSeconds = 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					MinigamesMod.queueServerWork(10, () -> {
						MinigamesModVariables.MapVariables.get(world).canGrabCrown = true;
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
						StartPVPProcedure.execute(world);
					});
				}
			} else if (MinigamesModVariables.MapVariables.get(world).returnToCastle == false) {
				if (MinigamesModVariables.MapVariables.get(world).gameHours == 0 && MinigamesModVariables.MapVariables.get(world).gameMinutes == 0 && MinigamesModVariables.MapVariables.get(world).gameSeconds == 0) {
					OnWinCrownHuntProcedure.execute(world);
				}
			}
		}
	}
}