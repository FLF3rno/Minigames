package net.mcreator.minigames.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.mcreator.minigames.entity.PreacherEntity;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.PreachingShotEntity;
public class PreacherTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof PreacherEntity _datEntI ? _datEntI.getEntityData().get(PreacherEntity.DATA_cooldown) : 0) == 40) {
			if (entity instanceof PreacherEntity _ent1) {
				_ent1.getEntityData().set(PreacherEntity.ANIM, 1000);
				_ent1.getEntityData().set(PreacherEntity.ANIM, 0);
			}
		} else if ((entity instanceof PreacherEntity _datEntI ? _datEntI.getEntityData().get(PreacherEntity.DATA_cooldown) : 0) == 90) {
			if (entity instanceof PreacherEntity _ent3) {
				_ent3.getEntityData().set(PreacherEntity.ANIM, 1000);
				_ent3.getEntityData().set(PreacherEntity.ANIM, 1);
			}
		}  else if ((entity instanceof PreacherEntity _datEntI ? _datEntI.getEntityData().get(PreacherEntity.DATA_cooldown) : 0) == 130) {

			Entity _shootFrom = entity;
			Level projectileLevel = _shootFrom.level();

			if (!projectileLevel.isClientSide()) {

				Projectile _entityToSpawn = new PreachingShotEntity(MinigamesModEntities.PREACHING_SHOT.get(), projectileLevel);

				_entityToSpawn.setPos(
						_shootFrom.getX(),
						_shootFrom.getEyeY() - 0.1,
						_shootFrom.getZ()
				);
				_entityToSpawn.setOwner(_shootFrom);

				if (_shootFrom instanceof Mob mob && mob.getTarget() != null) {

					LivingEntity target = mob.getTarget();

					double dx = target.getX() - _shootFrom.getX();
					double dy = target.getEyeY() - _entityToSpawn.getY();
					double dz = target.getZ() - _shootFrom.getZ();

					_entityToSpawn.shoot(dx, dy, dz, 1.0F, 0.0F);
				} else {
					_entityToSpawn.shoot(
							_shootFrom.getLookAngle().x,
							_shootFrom.getLookAngle().y,
							_shootFrom.getLookAngle().z,
							1.0F,
							0.0F
					);
				}

				projectileLevel.addFreshEntity(_entityToSpawn);
			}

			if (entity instanceof PreacherEntity _datEntSetI)
				_datEntSetI.getEntityData().set(PreacherEntity.DATA_cooldown, 0);
		}
		if (entity instanceof PreacherEntity _datEntSetI)
			_datEntSetI.getEntityData().set(PreacherEntity.DATA_cooldown, (int) ((entity instanceof PreacherEntity _datEntI ? _datEntI.getEntityData().get(PreacherEntity.DATA_cooldown) : 0) + 1));
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