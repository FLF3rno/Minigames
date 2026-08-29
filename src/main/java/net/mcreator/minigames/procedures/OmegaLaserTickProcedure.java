package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.FlavioSweeperEntity;
import net.mcreator.minigames.entity.FlavioTrapdoor2Entity;
import net.mcreator.minigames.entity.FlavioTrapdoor3Entity;
import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
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

import java.util.Comparator;

public class OmegaLaserTickProcedure {
	private static boolean tracking;
	private static float lockedYaw;
	private static float bodyYaw;
	private static float lockedPitch;
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean attack = false;
		boolean telegraph = false;

		if (entity.tickCount < 35) {
			{
				entity.setYRot(entity.getYRot() + 10);
				entity.setXRot(0);
				entity.yRotO = entity.getYRot();
				entity.xRotO = entity.getXRot();

				if (entity instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}

			{
				double _ty = y + 0.12;

				entity.teleportTo(x, _ty, z);

				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(
							x,
							_ty,
							z,
							entity.getYRot(),
							entity.getXRot()
					);
			}
		} else if (entity.tickCount == 35) {
			{
				entity.setYRot(0);
				entity.setXRot(0);
				entity.yRotO = entity.getYRot();
				entity.xRotO = entity.getXRot();

				if (entity instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		} else if (entity.tickCount > 65) {
			if (entity.tickCount % 120 == 0) {
				entity.getEntityData().set(FlavioSweeperEntity.ANIM, 1000);
				entity.getEntityData().set(FlavioSweeperEntity.ANIM, 0);

				tracking = true;

				bodyYaw = entity.getYRot();
			}
			if (entity.tickCount % 120 == 60) {
				tracking = false;

				lockedYaw = entity.getYRot();
				lockedPitch = entity.getXRot();
			}
		}

		if (tracking) {
			if (world.isClientSide())
				return;

			Player player = (Player)findEntityInWorldRange(world, Player.class, x, y, z, 60);
            if (player != null) {
                smoothLookAt(entity, player.getEyePosition(), 4.0F);
            }
		} else {
			if (world.isClientSide())
				return;

		}
	}
	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
	private static void smoothLookAt(Entity entity, Vec3 target, float maxTurn) {
		Vec3 eyes = entity.getEyePosition();
		double dx = target.x - eyes.x;
		double dy = target.y - eyes.y;
		double dz = target.z - eyes.z;
		double horizontal = Math.sqrt(dx * dx + dz * dz);
		float targetYaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
		float targetPitch = (float)-Math.toDegrees(Math.atan2(dy, horizontal));
		entity.setYRot(net.minecraft.util.Mth.approachDegrees(entity.getYRot(), targetYaw, maxTurn));
		entity.setXRot(net.minecraft.util.Mth.approachDegrees(entity.getXRot(), targetPitch, maxTurn));
	}

	private static float rotateTowards(float current, float target, float maxChange) {
		float delta = net.minecraft.util.Mth.wrapDegrees(target - current);

		if (delta > maxChange)
			delta = maxChange;
		if (delta < -maxChange)
			delta = -maxChange;

		return current + delta;
	}

}
