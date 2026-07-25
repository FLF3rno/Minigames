package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.GrapplingHitboxEntity;
import net.mcreator.minigames.entity.GrappleEntity;

public class GrapplingHookRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null) return;

		// Detect whether the hook is already active by looking for an existing hitbox on the server
		GrapplingHitboxEntity existingHitbox = null;
		if (world instanceof ServerLevel _level) {
			existingHitbox = _level.getEntitiesOfClass(
					GrapplingHitboxEntity.class,
					new AABB(new Vec3(x, y, z), new Vec3(x, y, z)).inflate(128)
			).stream()
					.filter(h -> entity.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner)))
					.findFirst().orElse(null);
		}

		if (existingHitbox == null) {
			// ── First click: fire the hook ───────────────────────────────────
			if (world instanceof Level _level) {
				_level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
			}
			if (entity instanceof LivingEntity _living) {
				InteractionHand _hand = _living.getMainHandItem() == itemstack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
				itemstack.hurtAndBreak(1, _living, _hand);
			}

			// Spawn projectile
			if (world instanceof Level projectileLevel && !projectileLevel.isClientSide()) {
				Entity _shootFrom = entity;
				Projectile _entityToSpawn = initArrowProjectile(
						new GrappleEntity(MinigamesModEntities.GRAPPLE.get(), projectileLevel),
						_shootFrom, 0, true, false, false, AbstractArrow.Pickup.DISALLOWED
				);
				_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
				_entityToSpawn.shoot(
						_shootFrom.getLookAngle().x,
						_shootFrom.getLookAngle().y,
						_shootFrom.getLookAngle().z,
						1.95f, 0
				);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}

			// Spawn the hitbox entity (invisible, leashed to shooter — provides the rope visual)
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = MinigamesModEntities.GRAPPLING_HITBOX.get().spawn(
						_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED
				);
				if (entityToSpawn instanceof GrapplingHitboxEntity hitbox) {
					hitbox.setLeashedTo(entity, true);
					hitbox.getEntityData().set(GrapplingHitboxEntity.DATA_owner, entity.getStringUUID());
				}
			}

		} else {
			// ── Second click: early release ───────────────────────────────────
			if (world instanceof Level _level) {
				_level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
			}
			if (world instanceof ServerLevel _level) {
				// Trigger early release on the hitbox (launches target, discards itself)
				existingHitbox.earlyRelease();

				// Also discard the flying projectile
				GrappleEntity grapple = _level.getEntitiesOfClass(
						GrappleEntity.class,
						new AABB(new Vec3(x, y, z), new Vec3(x, y, z)).inflate(128)
				).stream()
						.filter(g -> g.getOwner() != null && g.getOwner().getStringUUID().equals(entity.getStringUUID()))
						.findFirst().orElse(null);
				if (grapple != null) {
					grapple.discard();
				}
			}
			itemstack.shrink(1);
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
