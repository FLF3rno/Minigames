package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class ApplyScreenshakeProcedure {
	private static double startIntensity = 0.0;
	private static long startTime = 0;
	private static long durationMs = 0;

	public static void execute(double newIntensity, double newDurationTicks) {
		startIntensity = newIntensity;
		startTime = System.currentTimeMillis();

		durationMs = (long) (newDurationTicks * 50.0);
	}

	public static double getIntensity() {
		long now = System.currentTimeMillis();
		long elapsedMs = now - startTime;

		if (elapsedMs >= durationMs || durationMs <= 0) {
			return 0.0;
		}

		double progress = (double) elapsedMs / durationMs;

		double fade = 1.0 - progress;
		fade = fade * fade;

		return startIntensity * fade;
	}
}