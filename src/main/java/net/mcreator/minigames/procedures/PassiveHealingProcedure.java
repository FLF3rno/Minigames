package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PassiveHealingProcedure {
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
		if (MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) >= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)) {
				if (!(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).healCD >= entity.getData(MinigamesModVariables.PLAYER_VARIABLES).PassiveHealCooldown)) {
					{
						MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.healCD = 0;
						_vars.markSyncDirty();
					}
					DungeonHealProcedure.execute(entity, entity.getData(MinigamesModVariables.PLAYER_VARIABLES).PassiveHealAmount, "passive");
				} else {
					{
						MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.healCD = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).healCD + 1;
						_vars.markSyncDirty();
					}
				}
			}
		}
	}
}