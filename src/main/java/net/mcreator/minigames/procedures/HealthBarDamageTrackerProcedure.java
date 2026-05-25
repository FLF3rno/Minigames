package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

@EventBusSubscriber
public class HealthBarDamageTrackerProcedure {
	@SubscribeEvent
	public static void onEntityDamage(LivingDamageEvent.Post event) {
		Entity entity = event.getEntity();
		if (entity == null) return;
		if (!entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minigames:dungeon")))) return;
		
		long currentTime = System.currentTimeMillis();
		entity.getPersistentData().putLong("healthbarLastDamageTime", currentTime);
	}
}
