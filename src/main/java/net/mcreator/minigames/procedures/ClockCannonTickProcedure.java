package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.CannonballEntity;
import net.mcreator.minigames.entity.FlavioClockCannonEntity;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class ClockCannonTickProcedure {
	private static int rotBonus;
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		boolean attack = false;

		if (entity.tickCount < 35) {
			{
				Entity _ent = entity;
				_ent.setYRot(entity.getYRot() + 10);
				_ent.setXRot(0);
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();

				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}

			{
				Entity _ent = entity;
				double _tx = x;
				double _ty = y + 0.12;
				double _tz = z;

				_ent.teleportTo(_tx, _ty, _tz);

				if (_ent instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
			}
		} else if (entity.tickCount == 35) {
			{
				Entity _ent = entity;
				_ent.setYRot(0);
				_ent.setXRot(0);
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();

				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		} else if (entity.tickCount > 65) {
			if (entity.tickCount == 66) { if (entity instanceof FlavioClockCannonEntity _ent3) { _ent3.getEntityData().set(FlavioClockCannonEntity.ANIM, 1000); _ent3.getEntityData().set(FlavioClockCannonEntity.ANIM, 0);}}
			if (entity.tickCount % 8 == 0) {
				attack = true;
				rotBonus -= 15;
				if (rotBonus < 0)
					rotBonus = 360;
			}
			if (entity.tickCount % 16 == 0) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y + 2.5, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:clock_tick_1")), SoundSource.BLOCKS, (float) 0.3, (float) 1);
					}
				}
			}
			if (entity.tickCount % 16 == 8) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y + 2.5, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:clock_tick_2")), SoundSource.BLOCKS, (float) 0.3, (float) 1);
					}
				}
			}
		}

		if (attack) {
			double yoffset = y + 1.5;
			float damage = 20;
			if (world instanceof ServerLevel projectileLevel) {
				Projectile _entityToSpawn = initArrowProjectile(new CannonballEntity(MinigamesModEntities.CANNONBALL.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, 1, (byte) 10)), entity, damage, true, false, false,
						AbstractArrow.Pickup.DISALLOWED);
				_entityToSpawn.setPos(x, yoffset, z);
				_entityToSpawn.shootFromRotation(entity, 0, rotBonus,0, 0.2F, 0);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}
			if (world instanceof ServerLevel projectileLevel) {
				Projectile _entityToSpawn = initArrowProjectile(new CannonballEntity(MinigamesModEntities.CANNONBALL.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, 1, (byte) 10)), entity, damage, true, false, false,
						AbstractArrow.Pickup.DISALLOWED);
				_entityToSpawn.setPos(x, yoffset, z);
				_entityToSpawn.shootFromRotation(entity, 0, 90 + rotBonus,0, 0.2F, 0);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}if (world instanceof ServerLevel projectileLevel) {
				Projectile _entityToSpawn = initArrowProjectile(new CannonballEntity(MinigamesModEntities.CANNONBALL.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, 1, (byte) 10)), entity, damage, true, false, false,
						AbstractArrow.Pickup.DISALLOWED);
				_entityToSpawn.setPos(x, yoffset, z);
				_entityToSpawn.shootFromRotation(entity, 0, 180 + rotBonus,0, 0.2F, 0);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}
			if (world instanceof ServerLevel projectileLevel) {
				Projectile _entityToSpawn = initArrowProjectile(new CannonballEntity(MinigamesModEntities.CANNONBALL.get(), 0, 0, 0, projectileLevel, createArrowWeaponItemStack(projectileLevel, 1, (byte) 10)), entity, damage, true, false, false,
						AbstractArrow.Pickup.DISALLOWED);
				_entityToSpawn.setPos(x, yoffset, z);
				_entityToSpawn.shootFromRotation(entity, 0, 270 + rotBonus,0, 0.2F, 0);
				projectileLevel.addFreshEntity(_entityToSpawn);
			}


			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, yoffset, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:cannon")), SoundSource.BLOCKS, (float) 0.1, (float) 1);
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