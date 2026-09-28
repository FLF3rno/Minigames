package net.mcreator.minigames.entity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.minigames.init.MinigamesModEntities;

import javax.annotation.Nullable;

public class CactusArrowEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);
	private int knockback = 0;
	private boolean canSplit = true;
	private double baseDamage = 2.0;
	private int groundTicks = 0;

	public CactusArrowEntity(EntityType<? extends CactusArrowEntity> type, Level world) {
		super(type, world);
		setNoGravity(false);
		this.pickup = Pickup.DISALLOWED;
	}

	public CactusArrowEntity(EntityType<? extends CactusArrowEntity> type, double x, double y, double z, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, x, y, z, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(false);
		this.pickup = Pickup.DISALLOWED;
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	public CactusArrowEntity(EntityType<? extends CactusArrowEntity> type, LivingEntity entity, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, entity, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(false);
		this.pickup = Pickup.DISALLOWED;
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	@Override
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(Blocks.AIR);
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
		} else {
			super.doKnockback(livingEntity, damageSource);
		}
	}

	@Override
	public void setBaseDamage(double baseDamage) {
		super.setBaseDamage(baseDamage);
		this.baseDamage = baseDamage;
	}

	public double getBaseDamageValue() {
		return this.baseDamage;
	}

	public boolean canSplit() {
		if (this.getPersistentData().contains("CanSplit")) {
			return this.getPersistentData().getBooleanOr("CanSplit", true);
		}
		return this.canSplit;
	}

	public void setCanSplit(boolean canSplit) {
		this.canSplit = canSplit;
		this.getPersistentData().putBoolean("CanSplit", canSplit);
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		double speed = Math.max(1.5, this.getDeltaMovement().length());
		Entity owner = this.getOwner();
		double damage = this.getBaseDamageValue();
		int kb = this.knockback;
		boolean crit = this.isCritArrow();
		boolean shouldSplit = this.canSplit() && !this.level().isClientSide();
		if (shouldSplit) {
			this.setCanSplit(false);
		}

		super.onHitEntity(entityHitResult);

		if (shouldSplit) {
			Entity hitEntity = entityHitResult.getEntity();
			if (hitEntity != null) {
				spawnSplitArrows(this.level(), hitEntity, owner, speed, damage, kb, crit);
			}
		}
	}

	private void spawnSplitArrows(Level level, Entity hitEntity, @Nullable Entity owner, double speed, double damage, int knockback, boolean crit) {
		double centerX = hitEntity.getX();
		double centerY = hitEntity.getY() + hitEntity.getBbHeight() * 0.5;
		double centerZ = hitEntity.getZ();
		double offset = Math.max(0.4, hitEntity.getBbWidth() * 0.5 + 0.2);

		int[][] directions = new int[][]{
			{0, -1},
			{0, 1},
			{1, 0},
			{-1, 0}
		};

		for (int[] dir : directions) {
			double dx = dir[0];
			double dz = dir[1];
			double spawnX = centerX + dx * offset;
			double spawnZ = centerZ + dz * offset;

			CactusArrowEntity splitArrow = new CactusArrowEntity(MinigamesModEntities.CACTUS_ARROW.get(), spawnX, centerY, spawnZ, level, null);
			splitArrow.setCanSplit(false);
			if (owner != null) {
				splitArrow.setOwner(owner);
			}
			splitArrow.setBaseDamage(damage);
			splitArrow.setKnockback(knockback);
			splitArrow.setCritArrow(crit);
			splitArrow.setSilent(true);
			splitArrow.pickup = Pickup.DISALLOWED;
			splitArrow.shoot(dx, 0.0, dz, (float) speed - 0.5F, 0.0F);
			level.addFreshEntity(splitArrow);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.isInGround()) {
			Vec3 movement = this.getDeltaMovement();
			double horizontalDistance = movement.horizontalDistance();
			this.setXRot((float) (Math.atan2(movement.y, horizontalDistance) * 180.0D / Math.PI));
			this.setYRot((float) (Math.atan2(movement.x, movement.z) * 180.0D / Math.PI));
		} else {
			this.groundTicks++;
			if (this.groundTicks >= 60) {
				this.discard();
			}
		}
	}

	@Override
	public void playerTouch(Player player) {
	}

	public static CactusArrowEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static CactusArrowEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 1f, 5, 5);
	}

	public static CactusArrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		CactusArrowEntity entityarrow = new CactusArrowEntity(MinigamesModEntities.CACTUS_ARROW.get(), entity, world, null);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(true);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		return entityarrow;
	}

	public static CactusArrowEntity shoot(LivingEntity entity, LivingEntity target) {
		CactusArrowEntity entityarrow = new CactusArrowEntity(MinigamesModEntities.CACTUS_ARROW.get(), entity, entity.level(), null);
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(true);
		entity.level().addFreshEntity(entityarrow);
		return entityarrow;
	}
}
