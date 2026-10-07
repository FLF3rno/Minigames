package net.mcreator.minigames.gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class GravityOrientation {
	private GravityOrientation() {}

	private static final Quaternionf[] CAMERA_ROTATIONS = new Quaternionf[6];
	static {
		CAMERA_ROTATIONS[0] = new Quaternionf();
		CAMERA_ROTATIONS[1] = new Quaternionf().rotationZ((float) Math.toRadians(-180));
		CAMERA_ROTATIONS[2] = new Quaternionf().rotationX((float) Math.toRadians(90));
		CAMERA_ROTATIONS[3] = new Quaternionf().rotationX((float) Math.toRadians(-90)).mul(new Quaternionf().rotationY((float) Math.toRadians(-180)));
		CAMERA_ROTATIONS[4] = new Quaternionf().rotationY((float) Math.toRadians(90)).mul(new Quaternionf().rotationX((float) Math.toRadians(90)));
		CAMERA_ROTATIONS[5] = new Quaternionf().rotationX((float) Math.toRadians(90)).mul(new Quaternionf().rotationZ((float) Math.toRadians(90)));
	}

	public static Quaternionf getCameraRotation(Direction gravity) {
		if (gravity == null) return new Quaternionf(CAMERA_ROTATIONS[0]);
		return new Quaternionf(CAMERA_ROTATIONS[gravity.get3DDataValue()]);
	}

	public static Quaternionf getRotation(Direction gravity) {
		return getCameraRotation(gravity);
	}

	public static Vec3 vecPlayerToWorld(double x, double y, double z, Direction gravity) {
		if (gravity == null || gravity == Direction.DOWN) return new Vec3(x, y, z);
		return switch (gravity) {
			case DOWN  -> new Vec3( x,  y,  z);
			case UP    -> new Vec3(-x, -y,  z);
			case NORTH -> new Vec3( x, -z,  y);
			case SOUTH -> new Vec3(-x, -z, -y);
			case WEST  -> new Vec3( y, -z, -x);
			case EAST  -> new Vec3(-y, -z,  x);
		};
	}

	public static Vec3 vecPlayerToWorld(Vec3 vec, Direction gravity) {
		return vecPlayerToWorld(vec.x, vec.y, vec.z, gravity);
	}

	public static Vec3 vecWorldToPlayer(double x, double y, double z, Direction gravity) {
		if (gravity == null || gravity == Direction.DOWN) return new Vec3(x, y, z);
		return switch (gravity) {
			case DOWN  -> new Vec3( x,  y,  z);
			case UP    -> new Vec3(-x, -y,  z);
			case NORTH -> new Vec3( x,  z, -y);
			case SOUTH -> new Vec3(-x, -z, -y);
			case WEST  -> new Vec3(-z,  x, -y);
			case EAST  -> new Vec3( z, -x, -y);
		};
	}

	public static Vec3 vecWorldToPlayer(Vec3 vec, Direction gravity) {
		return vecWorldToPlayer(vec.x, vec.y, vec.z, gravity);
	}

	public static AABB boxPlayerToWorld(AABB box, Direction gravity) {
		if (gravity == null || gravity == Direction.DOWN) return box;
		double[] xs = new double[]{box.minX, box.maxX};
		double[] ys = new double[]{box.minY, box.maxY};
		double[] zs = new double[]{box.minZ, box.maxZ};
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
		for (double x : xs) {
			for (double y : ys) {
				for (double z : zs) {
					Vec3 v = vecPlayerToWorld(x, y, z, gravity);
					if (v.x < minX) minX = v.x;
					if (v.x > maxX) maxX = v.x;
					if (v.y < minY) minY = v.y;
					if (v.y > maxY) maxY = v.y;
					if (v.z < minZ) minZ = v.z;
					if (v.z > maxZ) maxZ = v.z;
				}
			}
		}
		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static AABB boxWorldToPlayer(AABB box, Direction gravity) {
		if (gravity == null || gravity == Direction.DOWN) return box;
		double[] xs = new double[]{box.minX, box.maxX};
		double[] ys = new double[]{box.minY, box.maxY};
		double[] zs = new double[]{box.minZ, box.maxZ};
		double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;
		for (double x : xs) {
			for (double y : ys) {
				for (double z : zs) {
					Vec3 v = vecWorldToPlayer(x, y, z, gravity);
					if (v.x < minX) minX = v.x;
					if (v.x > maxX) maxX = v.x;
					if (v.y < minY) minY = v.y;
					if (v.y > maxY) maxY = v.y;
					if (v.z < minZ) minZ = v.z;
					if (v.z > maxZ) maxZ = v.z;
				}
			}
		}
		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static Direction getRelativeDirection(Direction dir, Direction gravity) {
		if (gravity == null || gravity == Direction.DOWN) return dir;
		Vec3 local = vecWorldToPlayer(dir.getStepX(), dir.getStepY(), dir.getStepZ(), gravity);
		return Direction.getApproximateNearest(local);
	}

	public static Vec3 calculatePositionOffset(net.minecraft.world.entity.Entity entity, Direction prevGravity, Direction newGravity) {
		if (entity == null || prevGravity == null || newGravity == null || prevGravity == newGravity) {
			return Vec3.ZERO;
		}
		net.minecraft.world.entity.EntityDimensions dimensions = entity.getDimensions(entity.getPose());
		Direction relativeDirection = getRelativeDirection(newGravity, prevGravity);
		Vec3 relativePosOffset = switch (relativeDirection) {
			case DOWN -> Vec3.ZERO;
			case UP -> new Vec3(0.0D, dimensions.height() - 1.0E-6D, 0.0D);
			default -> relativeDirection.getUnitVec3()
				.scale(dimensions.width() / 2.0D - (newGravity.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0E-6D : 0.0D))
				.add(0.0D, dimensions.width() / 2.0D - (prevGravity.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0E-6D : 0.0D), 0.0D);
		};
		return vecPlayerToWorld(relativePosOffset, prevGravity);
	}

	public static Vec3 getDownVector(Direction gravity) {
		if (gravity == null) return new Vec3(0, -1, 0);
		return new Vec3(gravity.getStepX(), gravity.getStepY(), gravity.getStepZ());
	}

	public static Vec3 getUpVector(Direction gravity) {
		return getDownVector(gravity).scale(-1.0);
	}

	public static Vec3 toWorld(Vec3 local, Direction gravity) {
		return vecPlayerToWorld(local, gravity);
	}

	public static Vec3 toGravity(Vec3 world, Direction gravity) {
		return vecWorldToPlayer(world, gravity);
	}

	public static Vec3 getParallelComponent(Vec3 velocity, Direction gravity) {
		Vec3 down = getDownVector(gravity);
		double dot = velocity.dot(down);
		return down.scale(dot);
	}

	public static Vec3 getPerpendicularComponent(Vec3 velocity, Direction gravity) {
		return velocity.subtract(getParallelComponent(velocity, gravity));
	}
}
