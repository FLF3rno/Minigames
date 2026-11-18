package net.mcreator.minigames.entity;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.BoulderProjectileHitsBlockProcedure;
import net.mcreator.minigames.init.MinigamesModEntities;

import javax.annotation.Nullable;

public class BoulderEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.COBBLESTONE);
	private int knockback = 0;

	public BoulderEntity(EntityType<? extends BoulderEntity> type, Level world) {
		super(type, world);
		setNoGravity(true);
	}

	public BoulderEntity(EntityType<? extends BoulderEntity> type, double x, double y, double z, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, x, y, z, world, PROJECTILE_ITEM, firedFromWeapon);
		setNoGravity(true);
		if (firedFromWeapon != null)
			setKnockback(firedFromWeapon.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK)));
	}

	public BoulderEntity(EntityType<? extends BoulderEntity> type, LivingEntity entity, Level world, @Nullable ItemStack firedFromWeapon) {
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
		return new ItemStack(Blocks.COBBLESTONE);
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

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 projectilePosition, Vec3 deltaPosition) {
		double d0 = Double.MAX_VALUE;
		Entity entity = null;
		AABB lookupBox = this.getBoundingBox();
		for (Entity entity1 : this.level().getEntities(this, lookupBox, this::canHitEntity)) {
			if (entity1 == this.getOwner())
				continue;
			AABB aabb = entity1.getBoundingBox();
			if (aabb.intersects(lookupBox)) {
				double d1 = projectilePosition.distanceToSqr(projectilePosition);
				if (d1 < d0) {
					entity = entity1;
					d0 = d1;
				}
			}
		}
		return entity == null ? null : new EntityHitResult(entity);
	}

	private Direction determineHitDirection(AABB entityBox, AABB blockBox) {
		double dx = entityBox.getCenter().x - blockBox.getCenter().x;
		double dy = entityBox.getCenter().y - blockBox.getCenter().y;
		double dz = entityBox.getCenter().z - blockBox.getCenter().z;
		double absDx = Math.abs(dx);
		double absDy = Math.abs(dy);
		double absDz = Math.abs(dz);
		if (absDy > absDx && absDy > absDz) {
			return dy > 0 ? Direction.DOWN : Direction.UP;
		} else if (absDx > absDz) {
			return dx > 0 ? Direction.WEST : Direction.EAST;
		} else {
			return dz > 0 ? Direction.NORTH : Direction.SOUTH;
		}
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		BoulderProjectileHitsBlockProcedure.execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ());
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isNoPhysics()) {
			for (VoxelShape collision : this.level().getBlockCollisions(this, this.getBoundingBox())) {
				for (AABB blockAABB : collision.toAabbs()) {
					if (this.getBoundingBox().intersects(blockAABB)) {
						BlockPos blockPos = new BlockPos((int) blockAABB.minX, (int) blockAABB.minY, (int) blockAABB.minZ);
						Vec3 intersectionPoint = new Vec3((blockAABB.minX + blockAABB.maxX) / 2, (blockAABB.minY + blockAABB.maxY) / 2, (blockAABB.minZ + blockAABB.maxZ) / 2);
						Direction hitDirection = determineHitDirection(this.getBoundingBox(), blockAABB);
						this.hitTargetOrDeflectSelf(new BlockHitResult(intersectionPoint, hitDirection, blockPos, false));
					}
				}
			}
		}
		if (this.isInGround())
			this.discard();
	}

	public static BoulderEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static BoulderEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 1f, 5, 5);
	}

	public static BoulderEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		BoulderEntity entityarrow = new BoulderEntity(MinigamesModEntities.BOULDER.get(), entity, world, null);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static BoulderEntity shoot(LivingEntity entity, LivingEntity target) {
		BoulderEntity entityarrow = new BoulderEntity(MinigamesModEntities.BOULDER.get(), entity, entity.level(), null);
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}