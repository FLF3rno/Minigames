package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GiveGameCompassProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).joinFirstTime == false) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.joinFirstTime = true;
				_vars.markSyncDirty();
			}
			GrantGameCompassProcedure.execute(entity);
		}
	}
}