package net.mcreator.minigames.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.entity.GrappleEntity;
import net.mcreator.minigames.entity.GrapplingHitboxEntity;
import net.mcreator.minigames.init.MinigamesModEntities;

public class GrapplingHookRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null) return;
		if (entity instanceof LivingEntity living) {
			InteractionHand hand = living.getMainHandItem() == itemstack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			itemstack.hurtAndBreak(1, living, hand);
		}

		GrappleEntity activeGrapple = null;
		GrapplingHitboxEntity existingHitbox = null;
		if (world instanceof ServerLevel level) {
			activeGrapple = level.getEntitiesOfClass(
					GrappleEntity.class,
					new AABB(entity.position(), entity.position()).inflate(128)
			).stream()
					.filter(g -> g.getOwner() != null && entity.getStringUUID().equals(g.getOwner().getStringUUID()))
					.findFirst().orElse(null);

			existingHitbox = level.getEntitiesOfClass(
					GrapplingHitboxEntity.class,
					new AABB(entity.position(), entity.position()).inflate(128)
			).stream()
					.filter(h -> entity.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner)))
					.findFirst().orElse(null);
		}

		if (activeGrapple == null && existingHitbox == null) {
			if (world instanceof Level level) {
				level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
			}
			if (entity instanceof LivingEntity living) {
				InteractionHand hand = living.getMainHandItem() == itemstack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			}

			if (world instanceof Level projectileLevel && !projectileLevel.isClientSide()) {
				Entity shootFrom = entity;
				Projectile toSpawn = initArrowProjectile(
						new GrappleEntity(MinigamesModEntities.GRAPPLE.get(), projectileLevel),
						shootFrom, 0, true, false, false, AbstractArrow.Pickup.DISALLOWED
				);
				toSpawn.setPos(shootFrom.getX(), shootFrom.getEyeY() - 0.1, shootFrom.getZ());
				toSpawn.shoot(
						shootFrom.getLookAngle().x,
						shootFrom.getLookAngle().y,
						shootFrom.getLookAngle().z,
						1.95f, 0
				);
				projectileLevel.addFreshEntity(toSpawn);
			}

			if (world instanceof ServerLevel level) {
				Entity entityToSpawn = MinigamesModEntities.GRAPPLING_HITBOX.get().spawn(
						level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED
				);
				if (entityToSpawn instanceof GrapplingHitboxEntity hitbox) {
					hitbox.setLeashedTo(entity, true);
					hitbox.getEntityData().set(GrapplingHitboxEntity.DATA_owner, entity.getStringUUID());
				}
			}
			return;
		}

		if (world instanceof Level level) {
			level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
		}
		if (world instanceof ServerLevel level) {
			if (activeGrapple != null) {
				activeGrapple.releaseHook();
			}
			if (existingHitbox != null) {
				existingHitbox.earlyRelease();
			}
		}
	}

	private static AbstractArrow initArrowProjectile(AbstractArrow entityToSpawn, Entity shooter, float damage,
			boolean silent, boolean fire, boolean particles, AbstractArrow.Pickup pickup) {
		entityToSpawn.setOwner(shooter);
		entityToSpawn.setBaseDamage(damage);
		if (silent) entityToSpawn.setSilent(true);
		if (fire) entityToSpawn.igniteForSeconds(100);
		if (particles) entityToSpawn.setCritArrow(true);
		entityToSpawn.pickup = pickup;
		return entityToSpawn;
	}
}
