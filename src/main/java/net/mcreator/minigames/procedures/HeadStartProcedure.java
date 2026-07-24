package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class HeadStartProcedure {
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
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode && MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			if (!(entity.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID)) {
				if (TimerSecondsProcedure.execute(entity) < MinigamesModVariables.MapVariables.get(world).WhenPVPActive) {
					if (!(entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MinigamesModMobEffects.IMMOBILIZED))) {
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, (int) (MinigamesModVariables.MapVariables.get(world).WhenPVPActive * 20), 1, false, false));
						{
							MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.TimerColor = "DB2300";
							_vars.markSyncDirty();
						}
						for (Entity entityiterator : new ArrayList<>(world.players())) {
							if ((entityiterator.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID)) {
								{
									MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
									_vars.TimerColor = "FFCA47";
									_vars.markSyncDirty();
								}
							}
						}
					}
				} else if (TimerSecondsProcedure.execute(entity) > MinigamesModVariables.MapVariables.get(world).WhenPVPActive) {
					if (entity instanceof LivingEntity _livEnt5 && _livEnt5.hasEffect(MinigamesModMobEffects.IMMOBILIZED)) {
						if (entity instanceof LivingEntity _entity)
							_entity.removeEffect(MinigamesModMobEffects.IMMOBILIZED);
					}
				}
			}
		}
	}
}