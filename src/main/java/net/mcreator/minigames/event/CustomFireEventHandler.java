package net.mcreator.minigames.event;

import net.mcreator.minigames.CustomFireHelper;
import net.mcreator.minigames.network.CustomFireStateSyncMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

@EventBusSubscriber
public class CustomFireEventHandler {

	@SubscribeEvent(priority = net.neoforged.bus.api.EventPriority.HIGHEST)
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		if (CustomFireHelper.isHandlingCustomDamage()) {
			return;
		}
		LivingEntity living = event.getEntity();
		boolean isFire = event.getSource().is(DamageTypeTags.IS_FIRE);
		if (isFire && CustomFireHelper.isDungeonEntity(living)) {
			long gameTime = living.level().getGameTime();
			long lastFireHurt = living.getPersistentData().getLongOr("LastFireHurtTick", -100L);
			if (gameTime - lastFireHurt < 20L) {
				event.setCanceled(true);
				return;
			}
			living.getPersistentData().putLong("LastFireHurtTick", gameTime);
		}
		if (!event.getSource().is(DamageTypes.ON_FIRE)) {
			return;
		}
		if (!(living.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (!living.getPersistentData().contains("CustomFire_Damage")) {
			return;
		}
		double customDamage = living.getPersistentData().getDoubleOr("CustomFire_Damage", 0);
		if (customDamage <= 0) {
			event.setCanceled(true);
			return;
		}
		String uuidStr = living.getPersistentData().getStringOr("CustomFire_SourceUUID", "");
		Entity sourceEntity = null;
		if (!uuidStr.isEmpty()) {
			try {
				sourceEntity = serverLevel.getEntity(UUID.fromString(uuidStr));
			} catch (Exception ignored) {
			}
		}
		if (sourceEntity != null && sourceEntity.isAlive()) {
			event.setCanceled(true);
			DamageSource customSource = serverLevel.damageSources().source(DamageTypes.ON_FIRE, sourceEntity, sourceEntity);
			CustomFireHelper.setHandlingCustomDamage(true);
			try {
				living.hurtServer(serverLevel, customSource, (float) customDamage);
			} finally {
				CustomFireHelper.setHandlingCustomDamage(false);
			}
		} else {
			event.setAmount((float) customDamage);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (!entity.isOnFire()) {
			if (entity.level().isClientSide()) {
				CustomFireHelper.clearClientFire(entity.getId());
			} else if (entity.getRemainingFireTicks() <= 0 && entity.getPersistentData().contains("CustomFire_Color")) {
				CustomFireHelper.clearFire(entity);
			}
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity living && living.isOnFire()) {
			int color = CustomFireHelper.getFireColor(living);
			if (color != 0 && event.getEntity() instanceof ServerPlayer player) {
				PacketDistributor.sendToPlayer(player, new CustomFireStateSyncMessage(living.getId(), color));
			}
		}
	}
}
