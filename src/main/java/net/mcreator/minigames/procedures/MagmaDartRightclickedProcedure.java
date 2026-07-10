package net.mcreator.minigames.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.MagmaDartProjectileEntity;

public class MagmaDartRightclickedProcedure {
	private static final double INSTANT_EFFECT_DISTANCE = 3;

	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (MagmaDartPropertyValueProviderProcedure.shouldTriggerInstantly(entity)) {
			Vec3 instantEffectPos = entity.position().add(entity.getLookAngle().scale(INSTANT_EFFECT_DISTANCE));
			MagmaDartProjectileProjectileHitsBlockProcedure.execute(entity.level(), instantEffectPos.x, instantEffectPos.y, instantEffectPos.z, entity.getYRot() * (-1));
		} else {
			Entity _shootFrom = entity;
			Level projectileLevel = _shootFrom.level();
			if (!projectileLevel.isClientSide()) {
				Projectile _entityToSpawn = initArrowProjectile(new MagmaDartProjectileEntity(MinigamesModEntities.MAGMA_DART_PROJECTILE.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, 0, (byte) 10)), _shootFrom, 0,
						true, false, false, AbstractArrow.Pickup.DISALLOWED);
				_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
				_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 3, 0);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}
		}
		itemstack.shrink(1);
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
