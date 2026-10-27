package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

import java.util.UUID;

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
			String hunterUUID = MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID;
			if (hunterUUID == null || hunterUUID.isEmpty() || hunterUUID.equals("\"\""))
				return;
			if (TimerSecondsProcedure.execute(entity) < MinigamesModVariables.MapVariables.get(world).WhenPVPActive) {
				if ((entity.getStringUUID()).equals(hunterUUID)) {
					if (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.IMMOBILIZED)) {
						_livEnt.removeEffect(MinigamesModMobEffects.IMMOBILIZED);
					}
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					if (!_vars.TimerColor.equals("FFCA47")) {
						_vars.TimerColor = "FFCA47";
						_vars.markSyncDirty();
					}
				} else {
					if (!(entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MinigamesModMobEffects.IMMOBILIZED))) {
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, (int) (MinigamesModVariables.MapVariables.get(world).WhenPVPActive * 20), 1, false, false));
					}
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					if (!_vars.TimerColor.equals("DB2300")) {
						_vars.TimerColor = "DB2300";
						_vars.markSyncDirty();
					}
				}
			} else {
				if (entity instanceof LivingEntity _livEnt5 && _livEnt5.hasEffect(MinigamesModMobEffects.IMMOBILIZED)) {
					if (entity instanceof LivingEntity _entity)
						_entity.removeEffect(MinigamesModMobEffects.IMMOBILIZED);
				}
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				if (_vars.TimerColor.equals("DB2300") || _vars.TimerColor.equals("FFCA47")) {
					_vars.TimerColor = "C7C7C7";
					_vars.markSyncDirty();
				}
			}
		}
	}

	private static Entity getEntityFromUUID(ServerLevel level, String uuid) {
		try {
			return level.getEntity(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}