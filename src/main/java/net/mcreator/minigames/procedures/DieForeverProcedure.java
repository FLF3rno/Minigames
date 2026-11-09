package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DieForeverProcedure {
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
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.ASCENDING)) {
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).ascendingTimer <= 0) {
				MinigamesModVariables.MapVariables.get(world).removeEffects = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				LoseDungeonProcedure.execute(world);
			} else {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.ascendingTimer = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).ascendingTimer - 1;
					_vars.markSyncDirty();
				}
			}
		}
	}
}