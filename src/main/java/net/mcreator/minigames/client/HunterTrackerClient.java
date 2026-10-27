package net.mcreator.minigames.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HunterTrackerClient {
	private static final Map<String, double[]> DIMENSION_COORDS = new ConcurrentHashMap<>();
	private static String currentDimension = "";

	public static void update(String dimensionId, double x, double z, boolean isCurrent) {
		DIMENSION_COORDS.put(dimensionId, new double[]{x, z});
		if (isCurrent) {
			currentDimension = dimensionId;
		}
	}

	public static double[] getCoordsForDimension(String dimensionId) {
		return DIMENSION_COORDS.get(dimensionId);
	}

	public static String getCurrentDimension() {
		return currentDimension;
	}

	public static void clear() {
		DIMENSION_COORDS.clear();
		currentDimension = "";
	}
}
