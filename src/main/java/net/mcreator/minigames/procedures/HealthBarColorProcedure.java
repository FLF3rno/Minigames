package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HealthBarColorProcedure {
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
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minigames:dungeon")))) {
			if (entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MinigamesModMobEffects.BLESSED)) {
				entity.getPersistentData().putString("healthbarColor", "#8CFAFA");
			} else if (entity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(MobEffects.WITHER)) {
				entity.getPersistentData().putString("healthbarColor", "#191F18");
			} else if (entity instanceof LivingEntity _livEnt5 && _livEnt5.hasEffect(MobEffects.POISON)) {
				entity.getPersistentData().putString("healthbarColor", "#86E65C");
			} else {
				entity.getPersistentData().putString("healthbarColor", "dc");
			}
		}
	}
}