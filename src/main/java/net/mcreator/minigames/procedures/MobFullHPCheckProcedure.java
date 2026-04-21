package net.mcreator.minigames.procedures;

import net.mcreator.minigames.init.MinigamesModAttributes;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class MobFullHPCheckProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity livingEntity) || entity instanceof Player) {
			return;
		}

		var attributeInstance = livingEntity.getAttribute(MinigamesModAttributes.IS_MOB_FULL_HP);
		if (attributeInstance == null) {
			return;
		}

		attributeInstance.setBaseValue(livingEntity.getHealth() >= livingEntity.getMaxHealth() ? 1 : 0);
	}
}
