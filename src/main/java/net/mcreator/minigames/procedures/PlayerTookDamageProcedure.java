package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PlayerTookDamageProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity());
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get()))) {
			ApplyEffectProcedure.execute(entity, false, GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get())), "minigames:extra_damage"),
					GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get())), "minigames:effect_length"), "minigames:damage_boost");
		}
	}
}