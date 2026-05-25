package net.mcreator.minigames.gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class GravitySupport {
	private GravitySupport() {}

	public static boolean isSupportedInGravity(Entity entity, Direction gravity) {
		if (entity == null || gravity == null || gravity == Direction.DOWN) return entity != null && entity.onGround();
		Vec3 probe = GravityMath.down(gravity).scale(0.35D);
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
}
