package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.BlessedArrowEntity;

public class ShootProjectileProcedure {
	public static void execute(LevelAccessor world, Entity shooter, double damage, double inaccuracy, double knockback, double piercing, double speed, String type) {
		if (shooter == null || type == null)
			return;
		double cannonSpeed = 0;
		if (shooter.getPersistentData().getBooleanOr("humanCannonball", false)) {
			shooter.getPersistentData().putBoolean("humanCannonball", false);
			cannonSpeed = 3;
			{
				MinigamesModVariables.PlayerVariables _vars = shooter.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.performKnockback = new Vec3((shooter.getLookAngle().x * cannonSpeed), (shooter.getLookAngle().y * cannonSpeed), (shooter.getLookAngle().z * cannonSpeed));
				_vars.markSyncDirty();
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3((shooter.getX()), (shooter.getY()), (shooter.getZ())), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null)
								.withSuppressedOutput(),
						"/playsound minecraft:block.vault.close_shutter player @a ~ ~ ~ 1 0.7");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3((shooter.getX()), (shooter.getY()), (shooter.getZ())), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null)
								.withSuppressedOutput(),
						"/playsound minecraft:entity.dragon_fireball.explode player @a ~ ~ ~ 1 1.4");
		} else {
			if ((type).equals("arrow")) {
				{
					Entity _shootFrom = shooter;
					Level projectileLevel = _shootFrom.level();
					if (!projectileLevel.isClientSide()) {
						Projectile _entityToSpawn = initArrowProjectile(
								new Arrow(projectileLevel, 0, 0, 0, new Arrow(EntityType.ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, (int) knockback, (byte) piercing)), shooter,
								(float) (damage / speed), false, false, false, AbstractArrow.Pickup.DISALLOWED);
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
								new SpectralArrow(projectileLevel, 0, 0, 0, new SpectralArrow(EntityType.SPECTRAL_ARROW, projectileLevel).getPickupItemStackOrigin(), createArrowWeaponItemStack(projectileLevel, (int) knockback, (byte) piercing)),
								shooter, (float) (damage / speed), false, false, false, AbstractArrow.Pickup.DISALLOWED);
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
			} else if ((type).equals("blessed_cursed_crossbow")) {
				{
					Entity _shootFrom = shooter;
					Level projectileLevel = _shootFrom.level();
					if (!projectileLevel.isClientSide()) {
						Projectile _entityToSpawn = initArrowProjectile(new BlessedArrowEntity(MinigamesModEntities.BLESSED_ARROW.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, (int) knockback, (byte) piercing)),
								shooter, (float) (damage / speed), true, false, false, AbstractArrow.Pickup.DISALLOWED);
						_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
						_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, (float) speed, (float) inaccuracy);
						projectileLevel.addFreshEntity(_entityToSpawn);
					}
				}
				{
					Entity _ent = shooter;
					if (_ent.level() instanceof ServerLevel _serverLevel) {
						_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:self_damage")))),
								(float) GetItemAttributeProcedure.execute(shooter instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY, "minigames:extra_damage"));
					}
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