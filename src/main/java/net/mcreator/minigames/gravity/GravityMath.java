package net.mcreator.minigames.gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public final class GravityMath {
	private GravityMath() {}

	public static Vec3 down(Direction gravity) {
		return new Vec3(gravity.getStepX(), gravity.getStepY(), gravity.getStepZ());
	}

	public static Vec3 up(Direction gravity) {
		return down(gravity).scale(-1.0D);
	}

	public static Vec3 projectToPlane(Vec3 vector, Vec3 normal) {
		return vector.subtract(normal.scale(vector.dot(normal)));
	}

	public static Basis movementBasis(Direction gravity, Vec3 fallbackForward) {
		Vec3 down = down(gravity);
		Vec3 forward = projectToPlane(fallbackForward.normalize(), down);
		if (forward.lengthSqr() < 1.0E-6D) forward = projectToPlane(new Vec3(0, 0, 1), down);
		forward = forward.normalize();
		Vec3 right = down.cross(forward).normalize();
		return new Basis(forward, right, down);
	}

	public record Basis(Vec3 forward, Vec3 right, Vec3 down) {}
}
