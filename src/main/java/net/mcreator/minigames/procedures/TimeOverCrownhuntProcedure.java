package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.ArrayList;

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
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (!world.isClientSide()) {
				if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame == true) {
					if (MinigamesModVariables.MapVariables.get(world).canGrabCrown == false) {
						if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerHours == 0 && entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes == 0
								&& entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds == 0) {
							MinigamesModVariables.MapVariables.get(world).ShowTimer = false;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
							{
								MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
								_vars.timerSpeed = 0;
								_vars.markSyncDirty();
							}
							MinigamesModVariables.MapVariables.get(world).inGracePeriod = false;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
							{
								MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
								_vars.timerHours = 1;
								_vars.timerMinutes = 1;
								_vars.timerSeconds = 1;
								_vars.markSyncDirty();
							}
							MinigamesMod.queueServerWork(10, () -> {
								MinigamesModVariables.MapVariables.get(world).canGrabCrown = true;
								MinigamesModVariables.MapVariables.get(world).markSyncDirty();
								StartPVPProcedure.execute(world);
							});
							break;
						}
					} else if (MinigamesModVariables.MapVariables.get(world).returnToCastle == false) {
						if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerHours == 0 && entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes == 0
								&& entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds == 0) {
							OnWinCrownHuntProcedure.execute(world);
							break;
						}
					}
				}
			}
		}
	}
}