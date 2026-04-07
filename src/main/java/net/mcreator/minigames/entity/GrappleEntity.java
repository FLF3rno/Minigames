package net.mcreator.minigames.entity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.entity.GrapplingHitboxEntity;

import javax.annotation.Nullable;

public class GrappleEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.IRON_BARS);
	private int knockback = 0;
	private boolean released = false;

	public GrappleEntity(EntityType<? extends GrappleEntity> type, Level world) {
		super(type, world);
		setNoGravity(true);
	}

	public GrappleEntity(EntityType<? extends GrappleEntity> type, double x, double y, double z, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, x, y, z, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(true);
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	public GrappleEntity(EntityType<? extends GrappleEntity> type, LivingEntity entity, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, entity, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(true);
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	@Override
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(Blocks.IRON_BARS);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	@Override
	protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
		if (knockback > 0.0) {
			double d1 = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
			Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * d1);
			if (vec3.lengthSqr() > 0.0) {
				livingEntity.push(vec3.x, 0.1, vec3.z);
			}
		} else { // knockback might be set by firedFromWeapon passed into constructor
			super.doKnockback(livingEntity, damageSource);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		super.onHitEntity(hitResult);
		if (!this.level().isClientSide()) {
			Entity owner = this.getOwner();
			Entity target = hitResult.getEntity();
			if (owner != null && target != null) {
				GrapplingHitboxEntity hitbox = this.level().getEntitiesOfClass(GrapplingHitboxEntity.class, this.getBoundingBox().inflate(32)).stream()
						.filter(h -> owner.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner))).findFirst().orElse(null);
				if (hitbox != null) {
					hitbox.getEntityData().set(GrapplingHitboxEntity.DATA_target, target.getStringUUID());
				}
			}
		}
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (!this.level().isClientSide()) {
			releaseAndBreak(this.getOwner());
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			Entity owner = this.getOwner();
			if (owner != null && this.distanceTo(owner) >= 30.0) {
				releaseAndBreak(owner);
			}
			if (owner != null) {
				GrapplingHitboxEntity hitbox = this.level().getEntitiesOfClass(GrapplingHitboxEntity.class, this.getBoundingBox().inflate(32)).stream()
						.filter(h -> owner.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner))).findFirst().orElse(null);
				if (hitbox != null) {
					if (hitbox.getEntityData().get(GrapplingHitboxEntity.DATA_target).isEmpty()) {
						hitbox.setPos(this.getX(), this.getY(), this.getZ());
						hitbox.setDeltaMovement(0, 0, 0);
					}
				}
			}
		}
		if (this.isInGround())
			this.discard();
	}

	private void releaseAndBreak(Entity owner) {
		if (released || owner == null || this.level().isClientSide())
			return;
		released = true;
		if (owner instanceof LivingEntity livingOwner) {
			ItemStack main = livingOwner.getMainHandItem();
			ItemStack off = livingOwner.getOffhandItem();
			if (main.is(MinigamesModItems.GRAPPLING_HOOK.get())) {
				main.shrink(1);
			} else if (off.is(MinigamesModItems.GRAPPLING_HOOK.get())) {
				off.shrink(1);
			}
			this.level().playSound(null, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		GrapplingHitboxEntity hitbox = this.level().getEntitiesOfClass(GrapplingHitboxEntity.class, new AABB(this.position(), this.position()).inflate(64 / 2d)).stream()
				.filter(h -> owner.getStringUUID().equals(h.getEntityData().get(GrapplingHitboxEntity.DATA_owner))).findFirst().orElse(null);
		if (hitbox != null) {
			String targetId = hitbox.getEntityData().get(GrapplingHitboxEntity.DATA_target);
			if (!targetId.isEmpty()) {
				Entity target = this.level().getEntity(java.util.UUID.fromString(targetId));
				if (target != null) {
					Vec3 toOwner = owner.position().subtract(target.position());
					int pullTicks = hitbox.getEntityData().get(GrapplingHitboxEntity.DATA_pullTicks);
					double launchSpeed = Math.min(1.6, 0.3 + (pullTicks * 0.02));
					target.setDeltaMovement(toOwner.normalize().scale(launchSpeed));
					target.hurtMarked = true;
				}
			}
			hitbox.discard();
		}
		this.discard();
	}

	public void onTargetReached() {
		releaseAndBreak(this.getOwner());
	}

	public static GrappleEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 0, 0);
	}

	public static GrappleEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 1f, 0, 0);
	}

	public static GrappleEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		GrappleEntity entityarrow = new GrappleEntity(MinigamesModEntities.GRAPPLE.get(), entity, world, null);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static GrappleEntity shoot(LivingEntity entity, LivingEntity target) {
		GrappleEntity entityarrow = new GrappleEntity(MinigamesModEntities.GRAPPLE.get(), entity, entity.level(), null);
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(0);
		entityarrow.setKnockback(0);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
