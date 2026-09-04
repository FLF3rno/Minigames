package net.mcreator.minigames.entity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.init.MinigamesModEntities;

import javax.annotation.Nullable;

public class CannonballEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Items.IRON_NUGGET);
	private int knockback = 0;

	public CannonballEntity(EntityType<? extends CannonballEntity> type, Level world) {
		super(type, world);
		setNoGravity(true);
	}

	public CannonballEntity(EntityType<? extends CannonballEntity> type, double x, double y, double z, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, x, y, z, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(true);

		if (firedFromWeapon != null) setKnockback(firedFromWeapon.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK)));
	}

	public CannonballEntity(EntityType<? extends CannonballEntity> type, LivingEntity entity, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, entity, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(true);

		if (firedFromWeapon != null)
			setKnockback(firedFromWeapon.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK)));
	}

	@Override
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return PROJECTILE_ITEM.copy();
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	@Override
	protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
		if (knockback > 0) {
			double resistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));

			Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * resistance);

			if (vec3.lengthSqr() > 0.0)
				livingEntity.push(vec3.x, 0.1, vec3.z);
		} else {
			super.doKnockback(livingEntity, damageSource);
		}
	}

	@Override
	public void tick() {
		if (this.isRemoved())
			return;

		this.tickCount++;

		if (this.tickCount > 200) {
			this.discard();
			return;
		}

		Vec3 movement = this.getDeltaMovement();

		EntityHitResult entityHit = findHitEntity(this.position(), movement);

		if (entityHit != null)
			this.onHitEntity(entityHit);

		this.setPos(
				this.getX() + movement.x,
				this.getY() + movement.y,
				this.getZ() + movement.z
		);

		this.setDeltaMovement(movement);

		if (movement.lengthSqr() > 0.000001) {
			float yaw = (float) (Math.atan2(movement.z, movement.x) * 180.0 / Math.PI) - 90.0F;

			float pitch = (float) (-(Math.atan2(movement.y, Math.sqrt(movement.x * movement.x + movement.z * movement.z)) * 180.0 / Math.PI));

			this.setYRot(yaw);
			this.setXRot(pitch);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		Entity target = result.getEntity();
		if (!(target instanceof Player))
			return;
		super.onHitEntity(result);
	}

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 projectilePosition, Vec3 deltaPosition) {
		double closestDistance = Double.MAX_VALUE;
		Entity closestEntity = null;

		AABB lookupBox = this.getBoundingBox().expandTowards(deltaPosition).inflate(0.2);

		for (Entity entity : this.level().getEntities(
				this,
				lookupBox,
				this::canHitEntity
		)) {
			if (entity == this.getOwner())
				continue;

			AABB entityBox = entity.getBoundingBox();

			if (entityBox.intersects(lookupBox)) {
				double distance = projectilePosition.distanceToSqr(entityBox.getCenter());

				if (distance < closestDistance) {
					closestEntity = entity;
					closestDistance = distance;
				}
			}
		}

		return closestEntity == null
				? null
				: new EntityHitResult(closestEntity);
	}

	public static CannonballEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 0.4f, 5, 5);
	}

	public static CannonballEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 0.4f, 5, 5);
	}

	public static CannonballEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		CannonballEntity entityarrow = new CannonballEntity(MinigamesModEntities.CANNONBALL.get(), entity, world, null);

		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);

		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);

		world.addFreshEntity(entityarrow);

		return entityarrow;
	}

	public static CannonballEntity shoot(LivingEntity entity, LivingEntity target) {
		CannonballEntity entityarrow = new CannonballEntity(
				MinigamesModEntities.CANNONBALL.get(),
				entity,
				entity.level(),
				null
		);

		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();

		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 0.4f * 2, 12.0F);

		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);

		entity.level().addFreshEntity(entityarrow);

		return entityarrow;
	}
}