package net.mcreator.minigames.procedures;

import java.util.HashSet;
import java.util.Set;

import net.mcreator.minigames.entity.FlavioSweeperEntity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SweeperTickProcedure {

	private static final double ARM_LENGTH = 11.46875;
	private static final double ARM_THICKNESS = 0.5625;
	private static final double COLLISION_RADIUS = ARM_THICKNESS / 4.0;
	private static final float DAMAGE = 5.0F;
	private static final double KNOCKBACK = 0.6;
	private static final Set<java.util.UUID> HIT_THIS_SWEEP = new HashSet<>();
	private static int lastAnimationTick = -1;


	public static void execute(
			LevelAccessor world,
			double x,
			double y,
			double z,
			Entity entity) {

		if (entity == null)
			return;

		if (entity.tickCount < 35) {

			entity.setYRot(entity.getYRot() + 10);
			entity.setXRot(0);

			entity.setYBodyRot(entity.getYRot());
			entity.setYHeadRot(entity.getYRot());

			entity.yRotO = entity.getYRot();
			entity.xRotO = entity.getXRot();

			if (entity instanceof LivingEntity living) {
				living.yBodyRotO = living.getYRot();
				living.yHeadRotO = living.getYRot();
			}

			entity.teleportTo(
					x,
					y + 0.12,
					z
			);

			if (entity instanceof ServerPlayer player) {
				player.connection.teleport(
						x,
						y + 0.12,
						z,
						entity.getYRot(),
						entity.getXRot()
				);
			}

		} else if (entity.tickCount == 35) {

			entity.setYRot(0);
			entity.setXRot(0);

			entity.setYBodyRot(entity.getYRot());
			entity.setYHeadRot(entity.getYRot());

			entity.yRotO = entity.getYRot();
			entity.xRotO = entity.getXRot();

			if (entity instanceof LivingEntity living) {
				living.yBodyRotO = living.getYRot();
				living.yHeadRotO = living.getYRot();
			}
		}

		if (entity.tickCount == 70) {

			if (entity instanceof FlavioSweeperEntity sweeper) {

				sweeper.getEntityData().set(FlavioSweeperEntity.ANIM, 1000);
				sweeper.getEntityData().set(FlavioSweeperEntity.ANIM, 0);
				HIT_THIS_SWEEP.clear();
				lastAnimationTick = 0;
			}
		}

		if (entity.tickCount >= 70) {

			if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {

				int animationTick = (entity.tickCount - 70) % 120;

				double angleDegrees = -animationTick * 3.0;

				double angleRadians = Math.toRadians(angleDegrees);


				double dirX = Math.cos(angleRadians);
				double dirZ = Math.sin(angleRadians);


				Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());

				double searchRadius =
						ARM_LENGTH + COLLISION_RADIUS + 1.0;

				AABB searchBox = new AABB(center.x - searchRadius, center.y - 2.0, center.z - searchRadius, center.x + searchRadius, center.y + 2.0,
						center.z + searchRadius
				);

				for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class, searchBox)) {

					if (target == entity)
						continue;

					if (!target.isAlive())
						continue;

					Vec3 relative = new Vec3(target.getX() - center.x, target.getY() - center.y, target.getZ() - center.z);


					double targetHalfHeight = target.getBbHeight() / 2.0;

					double verticalDistance = Math.abs(relative.y);

					if (verticalDistance > targetHalfHeight + COLLISION_RADIUS) {
						continue;
					}


					double along = relative.x * dirX + relative.z * dirZ;

					if (Math.abs(along) > ARM_LENGTH + target.getBbWidth() / 2.0) {
						continue;
					}

					double perpendicular = Math.abs(relative.x * dirZ - relative.z * dirX);

					double targetRadius = target.getBbWidth() / 2.0;

					double collisionDistance = COLLISION_RADIUS + targetRadius;

					if (perpendicular <= collisionDistance) {

						target.hurtServer(serverLevel, serverLevel.damageSources().mobAttack((LivingEntity) entity), DAMAGE);

						double distance = Math.sqrt(relative.x * relative.x + relative.z * relative.z);

						if (distance > 0.001) {

							double knockX = relative.x / distance;

							double knockZ = relative.z / distance;

							target.push(knockX * KNOCKBACK, 0.25, knockZ * KNOCKBACK);

							target.hurtMarked = true;
						}

						HIT_THIS_SWEEP.add(target.getUUID());
					}
				}
			}
		}
	}
}