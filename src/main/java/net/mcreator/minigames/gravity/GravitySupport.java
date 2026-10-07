package net.mcreator.minigames.gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class GravitySupport {
	private GravitySupport() {}

	public static boolean isSupportedInGravity(Entity entity, Direction gravity) {
		if (entity == null || gravity == null || gravity == Direction.DOWN) return entity != null && entity.onGround();
		Vec3 probe = GravityOrientation.getDownVector(gravity).scale(0.01D);
		return !entity.level().noCollision(entity, entity.getBoundingBox().move(probe.x, probe.y, probe.z));
	}

	public static double supportGap(Entity entity, Direction gravity, double maxDistance) {
		if (entity == null || gravity == null || gravity == Direction.DOWN) return 0.0D;
		Vec3 dir = GravityMath.down(gravity).normalize();
		AABB base = entity.getBoundingBox();
		for (double d = 0.05D; d <= maxDistance; d += 0.05D) {
			Vec3 off = dir.scale(d);
			if (!entity.level().noCollision(entity, base.move(off.x, off.y, off.z))) return d;
		}
		return -1.0D;
	}

	public static net.minecraft.core.BlockPos getGroundBlockPos(Entity entity, Direction gravity) {
		AABB box = entity.getBoundingBox();
		double x = (box.minX + box.maxX) * 0.5;
		double y = (box.minY + box.maxY) * 0.5;
		double z = (box.minZ + box.maxZ) * 0.5;
		switch (gravity) {
			case DOWN -> y = box.minY - 0.2;
			case UP -> y = box.maxY + 0.2;
			case NORTH -> z = box.minZ - 0.2;
			case SOUTH -> z = box.maxZ + 0.2;
			case WEST -> x = box.minX - 0.2;
			case EAST -> x = box.maxX + 0.2;
		}
		return net.minecraft.core.BlockPos.containing(x, y, z);
	}
}
