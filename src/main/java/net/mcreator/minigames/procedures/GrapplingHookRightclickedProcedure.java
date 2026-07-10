package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.GrapplingHitboxEntity;
import net.mcreator.minigames.entity.GrappleEntity;

public class GrapplingHookRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("thrown", false) == false) {
			if (world instanceof Level _level) {
				_level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
			}
			if (entity instanceof LivingEntity _living) {
				InteractionHand _hand = _living.getMainHandItem() == itemstack ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
				itemstack.hurtAndBreak(1, _living, _hand);
			}
			{
				final String _tagName = "thrown";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putBoolean(_tagName, _tagValue));
			}
			{
				Entity _shootFrom = entity;
				Level projectileLevel = _shootFrom.level();
				if (!projectileLevel.isClientSide()) {
					Projectile _entityToSpawn = initArrowProjectile(new GrappleEntity(MinigamesModEntities.GRAPPLE.get(), projectileLevel), _shootFrom, 0, true, false, false, AbstractArrow.Pickup.DISALLOWED);
					_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
					_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) 1.95, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = MinigamesModEntities.GRAPPLING_HITBOX.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
				if (entityToSpawn instanceof GrapplingHitboxEntity hitbox) {
					hitbox.setLeashedTo(entity, true);
					hitbox.getEntityData().set(GrapplingHitboxEntity.DATA_owner, entity.getStringUUID());
				}
			}
		} else {
			if (world instanceof Level _level) {
				_level.playSound(null, x, y, z, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0f, 0.4f);
			}
			if (world instanceof ServerLevel _level) {
				GrapplingHitboxEntity hitbox = _level.getEntitiesOfClass(GrapplingHitboxEntity.class, new AABB(new Vec3(x, y, z), new Vec3(x, y, z)).inflate(64 / 2d)).stream()
						.filter(h -> entity.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner))).findFirst().orElse(null);
				if (hitbox != null) {
					String targetId = hitbox.getEntityData().get(GrapplingHitboxEntity.DATA_target);
					if (!targetId.isEmpty()) {
						Entity target = _level.getEntity(java.util.UUID.fromString(targetId));
						if (target != null) {
							Vec3 toOwner = entity.position().subtract(target.position());
							int pullTicks = hitbox.getEntityData().get(GrapplingHitboxEntity.DATA_pullTicks);
							double launchSpeed = Math.min(0.8, 0.15 + (pullTicks * 0.01));
							target.setDeltaMovement(toOwner.normalize().scale(launchSpeed));
							target.hurtMarked = true;
						}
					}
					hitbox.discard();
				}
				GrappleEntity grapple = _level.getEntitiesOfClass(GrappleEntity.class, new AABB(new Vec3(x, y, z), new Vec3(x, y, z)).inflate(128 / 2d)).stream()
						.filter(g -> g.getOwner() != null && g.getOwner().getStringUUID().equals(entity.getStringUUID())).findFirst().orElse(null);
				if (grapple != null) {
					grapple.discard();
				}
			}
			itemstack.shrink(1);
		}
	}

	private static AbstractArrow initArrowProjectile(AbstractArrow entityToSpawn, Entity shooter, float damage, boolean silent, boolean fire, boolean particles, AbstractArrow.Pickup pickup) {
		entityToSpawn.setOwner(shooter);
		entityToSpawn.setBaseDamage(damage);
		if (silent)
			entityToSpawn.setSilent(true);
		if (fire)
			entityToSpawn.igniteForSeconds(100);
		if (particles)
			entityToSpawn.setCritArrow(true);
		entityToSpawn.pickup = pickup;
		return entityToSpawn;
	}
}

