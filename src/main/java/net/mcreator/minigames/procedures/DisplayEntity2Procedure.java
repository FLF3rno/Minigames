package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class DisplayEntity2Procedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static Entity execute(LevelAccessor world) {
		return execute(null, world);
	}

	private static Entity execute(@Nullable Event event, LevelAccessor world) {
		Entity display = null;
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 2) {
				display = entityiterator;
			}
		}
		return display;
	}
}