package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class OnDungeonDeathProcedure {
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
		if (entity == null || world.isClientSide())
			return;

		String registryName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
		boolean isDungeonMob = entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minigames:dungeon"))) || registryName.contains("worshipper") || registryName.contains("candlehead")
				|| registryName.contains("shield_angel");

		if (isDungeonMob) {
			Entity killer = sourceentity;
			if (killer instanceof Projectile projectile && projectile.getOwner() != null) {
				killer = projectile.getOwner();
			}
			double baseCoins = 0;
			if (entity instanceof LivingEntity livingEntity) {
				if (livingEntity.getAttributes().hasAttribute(MinigamesModAttributes.DROPPED_COINS)) {
					baseCoins = livingEntity.getAttributeValue(MinigamesModAttributes.DROPPED_COINS);
				}
			}
			if (baseCoins <= 0) {
				if (registryName.contains("worshipper"))
					baseCoins = 1;
				else if (registryName.contains("candlehead"))
					baseCoins = 3;
				else if (registryName.contains("shield_angel"))
					baseCoins = 5;
			}
			double killBonus = 0.0;
			if (killer instanceof LivingEntity livingKiller) {
				if (livingKiller.getAttributes().hasAttribute(MinigamesModAttributes.COINS_ON_KILL)) {
					killBonus = (100.0 + livingKiller.getAttributeValue(MinigamesModAttributes.COINS_ON_KILL)) / 100.0;
				}
			}
			double playerMultiplier = world.players().size() * 0.75 + 0.25;
			double earned = baseCoins * killBonus * playerMultiplier;
			if (earned > 0) {
				MinigamesModVariables.MapVariables.get(world).dungeonCoins += earned;
				MinigamesModVariables.MapVariables.get(world).showCoins = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				MinigamesMod.queueServerWork(100, () -> {
					MinigamesModVariables.MapVariables.get(world).showCoins = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				});
			}
		}
	}
}
