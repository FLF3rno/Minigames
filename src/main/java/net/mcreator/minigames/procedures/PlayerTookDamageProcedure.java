package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PlayerTookDamageProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get()))) {
			ApplyEffectProcedure.execute(world, entity, false, GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get())), "minigames:extra_damage"),
					GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entity, new ItemStack(MinigamesModItems.REACTION_TIME.get())), "minigames:effect_length"), "minigames:damage_boost");
		}
	}
}