package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.entity.MovingBlockEntity;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.List;

public class MovingBlockTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.level().isClientSide())
			return;

		// 1. Calculate Intended Velocity
		double vx = (entity instanceof MovingBlockEntity _mbe ? _mbe.getEntityData().get(MovingBlockEntity.DATA_movingX) : 0) / 100.0;
		double vy = (entity instanceof MovingBlockEntity _mbe ? _mbe.getEntityData().get(MovingBlockEntity.DATA_movingY) : 0) / 100.0;
		double vz = (entity instanceof MovingBlockEntity _mbe ? _mbe.getEntityData().get(MovingBlockEntity.DATA_movingZ) : 0) / 100.0;

		// Pre-capture nearby riders and detect jump override.
		AABB preMoveTopBox = entity.getBoundingBox().inflate(0.05, 0.25, 0.05).move(0, 0.1, 0);
		List<Entity> riders = world.getEntitiesOfClass(Entity.class, preMoveTopBox, e -> e != entity);
		double surfaceY = entity.getY() + entity.getBbHeight();
		AABB platformBox = entity.getBoundingBox();
		boolean jumpOverrideYCollision = false;
		for (Entity target : riders) {
			if (!(target instanceof Player))
				continue;
			double playerFeetY = target.getBoundingBox().minY;
			boolean onTop = Math.abs(playerFeetY - surfaceY) <= 0.22 || target.getBoundingBox().intersects(platformBox.inflate(0.0, 0.04, 0.0));
			if (onTop && target.getData(MinigamesModVariables.PLAYER_VARIABLES).jumps) {
				jumpOverrideYCollision = true;
				break;
			}
		}

		// 2. COLLISION CHECK USING FULL ENTITY VOLUME (not just center ray)
		AABB box = entity.getBoundingBox();
		if (!entity.level().noBlockCollision(entity, box.move(vx, 0, 0)))
			vx = 0;
		if (!jumpOverrideYCollision && !entity.level().noBlockCollision(entity, box.move(0, vy, 0)))
			vy = 0;
		if (!entity.level().noBlockCollision(entity, box.move(0, 0, vz)))
			vz = 0;

		// 4. MOVE RIDERS FIRST (same delta as platform)
		for (Entity target : riders) {
			if (!(target instanceof Player))
				continue;
			double playerFeetY = target.getBoundingBox().minY;
			boolean onTop = Math.abs(playerFeetY - surfaceY) <= 0.22 || target.getBoundingBox().intersects(platformBox.inflate(0.0, 0.04, 0.0));
			if (!onTop)
				continue;

			// Same movement logic as platform for player.
			target.setPos(target.getX() + vx, target.getY() + vy, target.getZ() + vz);
			target.fallDistance = 0;
		}

		// 5. MOVE THE BLOCK
		// setPos is reliable for teleporting NoAi entities every tick.
		entity.setNoGravity(true);
		entity.setDeltaMovement(vx, vy, vz);
		entity.setPos(entity.getX() + vx, entity.getY() + vy, entity.getZ() + vz);
		entity.hurtMarked = true;

		// Minimal anti-tunneling guard: if rider overlaps platform, correct Y only.
		double movedSurfaceY = entity.getY() + entity.getBbHeight();
		AABB movedPlatformBox = entity.getBoundingBox();
		for (Entity target : riders) {
			if (!(target instanceof Player))
				continue;
			if (target.getBoundingBox().intersects(movedPlatformBox.inflate(0.0, 0.03, 0.0))) {
				target.setPos(target.getX(), movedSurfaceY + 0.01, target.getZ());
				target.fallDistance = 0;
			}
		}
	}
}
