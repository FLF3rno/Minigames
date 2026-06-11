package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModEntities;

public class ShootProjectileProcedure {
	public static void execute(LevelAccessor world, Entity shooter, double damage, double inaccuracy, double knockback, double piercing, double speed, String type) {
		if (shooter == null || type == null)
			return;
		if ((type).equals("arrow")) {
			{
				Entity _shootFrom = shooter;
				Level projectileLevel = _shootFrom.level();
				if (!projectileLevel.isClientSide()) {
					Projectile _entityToSpawn = initArrowProjectile(
							new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, (int) knockback, (byte) piercing)), shooter, (float) damage, false,
							false, false, AbstractArrow.Pickup.DISALLOWED);
					_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
					_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) speed, (float) inaccuracy);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
		} else if ((type).equals("spectral_arrow")) {
			{
				Entity _shootFrom = shooter;
				Level projectileLevel = _shootFrom.level();
				if (!projectileLevel.isClientSide()) {
					Projectile _entityToSpawn = initArrowProjectile(
							new SpectralArrow(projectileLevel, 0, 0, 0, new SpectralArrow(EntityType.SPECTRAL_ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, (int) knockback, (byte) piercing)), shooter,
							(float) damage, false, false, false, AbstractArrow.Pickup.DISALLOWED);
					_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
					_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) speed, (float) inaccuracy);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
		} else if ((type).equals("volleybomb")) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = MinigamesModEntities.VOLLEYBOMB_ENTITY.get().spawn(_level, BlockPos.containing(shooter.getX(), shooter.getY() + 1, shooter.getZ()), EntitySpawnReason.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(shooter.getYRot());
					entityToSpawn.setYBodyRot(shooter.getYRot());
					entityToSpawn.setYHeadRot(shooter.getYRot());
					entityToSpawn.setXRot(shooter.getXRot());
					entityToSpawn.setDeltaMovement((shooter.getLookAngle().x * speed), (shooter.getLookAngle().y * speed), (shooter.getLookAngle().z * speed));
				}
			}
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

	private static ItemStack createArrowWeaponItemStack(Level level, int knockback, byte piercing) {
		ItemStack weapon = new ItemStack(Items.ARROW);
		if (knockback > 0)
			weapon.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), knockback);
		if (piercing > 0)
			weapon.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PIERCING), piercing);
		return weapon;
	}
}