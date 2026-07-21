package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ManageTimerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).ShowTimer == true) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.timerTick = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerTick + entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSpeed;
				_vars.markSyncDirty();
			}
			while (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerTick >= 20) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerSeconds = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds + 1;
					_vars.timerTick = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerTick - 20;
					_vars.markSyncDirty();
				}
			}
			while (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds >= 60) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerMinutes = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes + 1;
					_vars.timerSeconds = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerSeconds - 60;
					_vars.markSyncDirty();
				}
			}
			while (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes >= 60) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerHours = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerHours + 1;
					_vars.timerMinutes = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).timerMinutes - 60;
					_vars.markSyncDirty();
				}
			}
		}
	}
}