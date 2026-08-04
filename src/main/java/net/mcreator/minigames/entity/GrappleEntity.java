package net.mcreator.minigames.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;
import java.util.UUID;

public class GrappleEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.IRON_BARS);
	private int knockback = 0;
	private boolean released = false;
	private String hookedTargetId = "";
	private int pullTicks = 0;

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
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		if (this.level().isClientSide()) {
			return;
		}
		Entity owner = this.getOwner();
		Entity target = hitResult.getEntity();
		if (owner != null && target != null && target != owner) {
			hookedTargetId = target.getStringUUID();
			pullTicks = 0;
			setDeltaMovement(Vec3.ZERO);
			setNoGravity(true);
		}
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		if (this.level().isClientSide()) {
			return;
		}

		BlockPos hitPos = blockHitResult.getBlockPos();
		BlockState hitState = this.level().getBlockState(hitPos);

		if (hitState.isAir() || !hitState.isSolid()) {
			releaseAndBreak(this.getOwner());
			return;
		}

		Entity owner = this.getOwner();
		if (owner == null) {
			releaseAndBreak(null);
			return;
		}

		this.level().removeBlock(hitPos, false);
		FallingBlockEntity falling = FallingBlockEntity.fall((Level) this.level(), hitPos, hitState);
		falling.setNoGravity(true);
		falling.setDeltaMovement(Vec3.ZERO);
		falling.time = 1;
		falling.dropItem = false;

		hookedTargetId = falling.getStringUUID();
		pullTicks = 0;
		setDeltaMovement(Vec3.ZERO);
		setNoGravity(true);
	}

	@Override
	public void tick() {
		super.tick();
		Entity owner = this.getOwner();
		if (owner == null) {
			if (!this.level().isClientSide()) {
				releaseAndBreak(null);
			}
			return;
		}

		if (!this.level().isClientSide()) {
			if (hookedTargetId.isEmpty() && this.distanceTo(owner) >= 30.0) {
				releaseAndBreak(owner);
				return;
			}

			if (!hookedTargetId.isEmpty()) {
				Entity target = resolveTarget();
				if (target == null || !target.isAlive()) {
					releaseAndBreak(owner);
					return;
				}

				pullTicks++;
				Vec3 ownerPos = owner.position().add(0, owner.getEyeHeight() * 0.5, 0);
				Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
				Vec3 toOwner = ownerPos.subtract(targetPos);
				double distance = toOwner.length();
				if (distance < 2.5D) {
					onTargetReached(owner, target, pullTicks);
					return;
				}

				double speed = Math.min(0.5D, 0.04D + pullTicks * 0.002D);
				Vec3 pull = toOwner.normalize().scale(speed);
				Vec3 current = target.getDeltaMovement();
				target.setDeltaMovement(
						current.x * 0.6 + pull.x,
						current.y * 0.6 + pull.y,
						current.z * 0.6 + pull.z
				);
				target.hurtMarked = true;
				if (target instanceof LivingEntity le) {
					le.fallDistance = 0f;
				}

				setPos(target.getX(), target.getEyeY() - 0.1, target.getZ());
				setDeltaMovement(Vec3.ZERO);
			}
		}
	}

	private void releaseAndBreak(@Nullable Entity owner) {
		if (released || this.level().isClientSide()) {
			return;
		}
		released = true;

		if (owner instanceof LivingEntity livingOwner) {
			this.level().playSound(null, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(),
					SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		hookedTargetId = "";
		this.discard();
	}

	private Entity resolveTarget() {
		if (!(this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) || hookedTargetId.isEmpty()) {
			return null;
		}
		try {
			return serverLevel.getEntity(UUID.fromString(hookedTargetId));
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}

	public void onTargetReached(Entity owner, Entity target, int pullTicks) {
		if (this.level().isClientSide()) {
			return;
		}
		Vec3 toOwner = owner.position().subtract(target.position()).normalize();
		double launchSpeed = Math.min(1.6, 0.15 + pullTicks * 0.01);
		target.setDeltaMovement(toOwner.scale(launchSpeed));
		target.hurtMarked = true;
		if (target instanceof FallingBlockEntity fallingBlock) {
			fallingBlock.setNoGravity(false);
		}
		hookedTargetId = "";
		this.discard();
	}

	public void releaseHook() {
		if (this.level().isClientSide()) {
			return;
		}
		Entity owner = this.getOwner();
		Entity target = resolveTarget();
		if (owner != null && target != null && target.isAlive()) {
			Vec3 toOwner = owner.position().subtract(target.position()).normalize();
			double launchSpeed = Math.min(1.6, 0.15 + pullTicks * 0.01);
			target.setDeltaMovement(toOwner.scale(launchSpeed));
			target.hurtMarked = true;
			if (target instanceof FallingBlockEntity fallingBlock) {
				fallingBlock.setNoGravity(false);
			}
		}
		hookedTargetId = "";
		this.discard();
	}

	public boolean hasHookedTarget() {
		return !hookedTargetId.isEmpty();
	}

	@Nullable
	public Entity getHookedTargetEntity() {
		return resolveTarget();
	}

	public boolean removeWhenFarAway(double d) {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public ItemStack getPickResult() {
		return ItemStack.EMPTY;
	}

	public static void init(net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return net.minecraft.world.entity.Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0)
				.add(Attributes.MOVEMENT_SPEED, 0.0);
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
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
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
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
