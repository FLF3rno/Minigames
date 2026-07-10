package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RemoveAllEffectsButCrownedProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).removeEffects) {
			MinigamesModVariables.MapVariables.get(world).removeEffects = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			clearAllEffects(entity, entity.getData(MinigamesModVariables.PLAYER_VARIABLES).isCrowned);
		}
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).removeEffectsSingleTarget) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.removeEffectsSingleTarget = false;
				_vars.markSyncDirty();
			}
			clearAllEffects(entity, entity.getData(MinigamesModVariables.PLAYER_VARIABLES).isCrowned);
		}
	}

	private static void clearAllEffects(Entity entity, boolean keepCrowned) {
		if (!(entity instanceof LivingEntity livingEntity))
			return;
		if (livingEntity.level().isClientSide())
			return;

		java.util.ArrayList<Holder<MobEffect>> effectsToRemove = new java.util.ArrayList<>(livingEntity.getActiveEffectsMap().keySet());
		for (Holder<MobEffect> effect : effectsToRemove) {
			if (keepCrowned && effect.is(MinigamesModMobEffects.CROWNED)) {
				continue;
			}
			livingEntity.removeEffect(effect);
		}

		if (keepCrowned && !livingEntity.hasEffect(MinigamesModMobEffects.CROWNED)) {
			livingEntity.addEffect(new net.minecraft.world.effect.MobEffectInstance(MinigamesModMobEffects.CROWNED, 1000000000, 0, false, false, true));
		}
	}
}
