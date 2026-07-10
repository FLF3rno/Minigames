package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class KilledDungeonMobProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		ItemStack itemstack = ItemStack.EMPTY;
		if (entity.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon")))) {
			itemstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
			if (itemstack.getItem() == MinigamesModItems.THE_FINISHER.get()) {
				MinigamesMod.queueServerWork(1, () -> {
					if (sourceentity instanceof Player _player)
						_player.getCooldowns().addCooldown((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), 0);
				});
			}
		}
	}
}