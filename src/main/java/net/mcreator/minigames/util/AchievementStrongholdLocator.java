package net.mcreator.minigames.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public final class AchievementStrongholdLocator {
	private AchievementStrongholdLocator() {
	}

	public static BlockPos getNearestTarget(ServerLevel level, BlockPos fromPos) {
		List<BlockPos> targets = AchievementStrongholdTargets.getTargets();
		if (targets.isEmpty()) {
			return null;
		}

		BlockPos nearest = null;
		double nearestDistance = Double.MAX_VALUE;
		for (BlockPos target : targets) {
			double distance = target.distSqr(fromPos);
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearest = target;
			}
		}
		return nearest;
	}

	public static BlockPos getDefaultTarget(ServerLevel level) {
		BlockPos target = AchievementStrongholdTargets.getPrimaryTarget();
		return target == BlockPos.ZERO && AchievementStrongholdTargets.getTargets().isEmpty() ? null : target;
	}
}
