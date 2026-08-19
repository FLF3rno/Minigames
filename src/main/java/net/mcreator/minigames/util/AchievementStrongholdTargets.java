package net.mcreator.minigames.util;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public final class AchievementStrongholdTargets {
	private static final List<BlockPos> TARGETS = new ArrayList<>();
	private static final Set<BlockPos> PLACED = new HashSet<>();

	private AchievementStrongholdTargets() {
	}

	public static void setTargets(List<BlockPos> positions) {
		TARGETS.clear();
		TARGETS.addAll(positions);
		PLACED.clear();
	}

	public static List<BlockPos> getTargets() {
		return Collections.unmodifiableList(TARGETS);
	}

	public static BlockPos getPrimaryTarget() {
		return TARGETS.isEmpty() ? BlockPos.ZERO : TARGETS.get(0);
	}

	public static boolean isEmpty() {
		return TARGETS.isEmpty();
	}

	public static boolean isPlaced(BlockPos pos) {
		return PLACED.contains(pos);
	}

	public static void markPlaced(BlockPos pos) {
		PLACED.add(pos);
	}
}
