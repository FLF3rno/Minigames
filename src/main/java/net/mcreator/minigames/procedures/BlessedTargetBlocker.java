package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.minigames.init.MinigamesModMobEffects;

@EventBusSubscriber
public class BlessedTargetBlocker {
	@SubscribeEvent
	public static void onTargetChange(LivingChangeTargetEvent event) {
		if (!(event.getEntity() instanceof Mob)) {
			return;
		}

		LivingEntity newTarget = event.getNewAboutToBeSetTarget();
		if (newTarget == null) {
			return;
		}

		if (newTarget.hasEffect(MinigamesModMobEffects.BLESSED)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		if (!(event.getEntity() instanceof Mob mob) || mob.level().isClientSide()) {
			return;
		}

		LivingEntity currentTarget = mob.getTarget();
		if (currentTarget != null && currentTarget.hasEffect(MinigamesModMobEffects.BLESSED)) {
			mob.setTarget(null);
		}
	}
}
