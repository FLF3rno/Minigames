package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SupportiveArrowHitProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		execute(null, world, entity, immediatesourceentity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		if (immediatesourceentity.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:arrow")))) {
			if (CheckRelicProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.SUPPORTIVE.get()))) {
				ApplyEffectProcedure.execute(world, entity, false, GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.SUPPORTIVE.get())), "minigames:effect_length"),
						GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.SUPPORTIVE.get())), "minigames:effect_length"), "minigames:speed_boost");
			}
		}
	}
}