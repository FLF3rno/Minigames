package net.mcreator.minigames.event;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class ObsidianTouchHandler {
	private static final Map<Integer, Long> LAST_DAMAGE_TICK = new ConcurrentHashMap<>();

	public static void checkTouching(ServerLevel level, LivingEntity obsidianEntity) {
		if (obsidianEntity == null || !obsidianEntity.isAlive()) {
			return;
		}
		AABB hitBox = obsidianEntity.getBoundingBox().inflate(0.12);
		List<LivingEntity> touching = level.getEntitiesOfClass(
			LivingEntity.class,
			hitBox,
			other -> other != obsidianEntity && other.isAlive() && !other.hasEffect(MinigamesModMobEffects.OBSIDIANIFICATION)
		);
		for (LivingEntity other : touching) {
			handleTouch(obsidianEntity, other, level);
		}
	}

	public static void handleTouch(LivingEntity obsidianEntity, LivingEntity touchedEntity, ServerLevel serverLevel) {
		if (obsidianEntity == null || touchedEntity == null || !touchedEntity.isAlive()) {
			return;
		}
		if (touchedEntity.hasEffect(MinigamesModMobEffects.OBSIDIANIFICATION)) {
			return;
		}

		long gameTime = serverLevel.getGameTime();
		Long lastTick = LAST_DAMAGE_TICK.get(touchedEntity.getId());
		if (lastTick != null && (gameTime - lastTick) < 5) {
			return;
		}

		double dx = touchedEntity.getX() - touchedEntity.xo;
		double dy = touchedEntity.getY() - touchedEntity.yo;
		double dz = touchedEntity.getZ() - touchedEntity.zo;
		double moveDist = Math.sqrt(dx * dx + dy * dy + dz * dz);
		double motionSpeed = touchedEntity.getDeltaMovement().length();
		double targetSpeed = Math.max(moveDist, motionSpeed);

		double obsDx = obsidianEntity.getX() - obsidianEntity.xo;
		double obsDy = obsidianEntity.getY() - obsidianEntity.yo;
		double obsDz = obsidianEntity.getZ() - obsidianEntity.zo;
		double obsDist = Math.sqrt(obsDx * obsDx + obsDy * obsDy + obsDz * obsDz);
		double obsMotion = obsidianEntity.getDeltaMovement().length();
		double obsSpeed = Math.max(obsDist, obsMotion);

		double speed = Math.max(targetSpeed, obsSpeed);

		// If barely moving, don't damage to avoid micro-damage when idling next to the entity
		if (speed < 0.08) {
			return;
		}

		LAST_DAMAGE_TICK.put(touchedEntity.getId(), gameTime);
		if (LAST_DAMAGE_TICK.size() > 100) {
			LAST_DAMAGE_TICK.clear();
		}

		float damage = (float) Math.max(1.0f, speed * 10.0f);
		DamageSource damageSource = new DamageSource(serverLevel.holderOrThrow(DamageTypes.FLY_INTO_WALL), obsidianEntity);
		touchedEntity.hurtServer(serverLevel, damageSource, damage);

		// Rebound slightly off the solid obsidian
		Vec3 motion = touchedEntity.getDeltaMovement();
		double reboundX = -motion.x * 0.4;
		double reboundZ = -motion.z * 0.4;
		double reboundY = Math.max(0.15, -motion.y * 0.4);
		if (Math.abs(reboundX) < 0.05 && Math.abs(reboundZ) < 0.05) {
			Vec3 diff = touchedEntity.position().subtract(obsidianEntity.position()).normalize();
			reboundX = diff.x * 0.2;
			reboundZ = diff.z * 0.2;
		}
		touchedEntity.setDeltaMovement(new Vec3(reboundX, reboundY, reboundZ));
		touchedEntity.hurtMarked = true;
		if (touchedEntity instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
		}

		serverLevel.playSound(null, touchedEntity.getX(), touchedEntity.getY(), touchedEntity.getZ(),
				SoundEvents.STONE_HIT, SoundSource.PLAYERS, 1.0f, 0.8f);
		if (speed > 0.6) {
			serverLevel.playSound(null, touchedEntity.getX(), touchedEntity.getY(), touchedEntity.getZ(),
					SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.5f, 1.3f);
		}
	}

	@SubscribeEvent
	public static void onEntityTickPost(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity living) || !living.isAlive()) {
			return;
		}
		if (living.level().isClientSide() || !(living.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (living.hasEffect(MinigamesModMobEffects.OBSIDIANIFICATION)) {
			return;
		}

		AABB box = living.getBoundingBox().inflate(0.06);
		List<LivingEntity> obsidianEntities = serverLevel.getEntitiesOfClass(
			LivingEntity.class,
			box,
			other -> other != living && other.isAlive() && other.hasEffect(MinigamesModMobEffects.OBSIDIANIFICATION)
		);
		for (LivingEntity obs : obsidianEntities) {
			handleTouch(obs, living, serverLevel);
		}
	}
}
