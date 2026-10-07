package net.mcreator.minigames.gravity.client;

import java.lang.ref.WeakReference;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

public final class GravityCameraTransition {
	private GravityCameraTransition() {}

	// Duration in game ticks for smooth camera rotation (9 ticks = ~450ms at 20 TPS)
	public static final float DURATION_TICKS = 9.0F;

	private static Direction lastTargetGravity = Direction.DOWN;
	private static final Quaternionf fromRotation = new Quaternionf();
	private static final Quaternionf toRotation = new Quaternionf();
	private static final Quaternionf currentRotation = new Quaternionf();
	private static float startTickTime = -1.0F;
	private static boolean initialized = false;
	private static WeakReference<Player> lastPlayerRef = new WeakReference<>(null);

	public static void update(Player player, float partialTicks) {
		if (player == null) return;

		Player lastPlayer = lastPlayerRef.get();
		if (lastPlayer != player) {
			lastPlayerRef = new WeakReference<>(player);
			initialized = false;
		}

		Direction targetGravity = GravityAccess.getGravity(player);
		if (targetGravity == null) targetGravity = Direction.DOWN;

		float currentTime = player.tickCount + partialTicks;

		if (!initialized) {
			lastTargetGravity = targetGravity;
			fromRotation.set(GravityOrientation.getCameraRotation(targetGravity));
			toRotation.set(fromRotation);
			currentRotation.set(fromRotation);
			startTickTime = currentTime - DURATION_TICKS;
			initialized = true;
			return;
		}

		if (targetGravity != lastTargetGravity) {
			// Save current interpolated rotation as the starting point for the new transition
			fromRotation.set(currentRotation);
			lastTargetGravity = targetGravity;
			toRotation.set(GravityOrientation.getCameraRotation(targetGravity));
			startTickTime = currentTime;
		}

		float elapsed = currentTime - startTickTime;
		if (elapsed < 0.0F || elapsed >= DURATION_TICKS) {
			currentRotation.set(toRotation);
		} else {
			float progress = Mth.clamp(elapsed / DURATION_TICKS, 0.0F, 1.0F);
			// Smoothstep easing: 3t^2 - 2t^3 for smooth start and finish
			float eased = progress * progress * (3.0F - 2.0F * progress);

			Quaternionf target = new Quaternionf(toRotation);
			float dot = fromRotation.x * target.x + fromRotation.y * target.y + fromRotation.z * target.z + fromRotation.w * target.w;
			if (dot < 0.0F) {
				target.set(-target.x, -target.y, -target.z, -target.w);
			}
			fromRotation.slerp(target, eased, currentRotation);
		}
	}

	public static Quaternionf getCurrentRotation() {
		return currentRotation;
	}

	public static boolean isTransitioning() {
		return Math.abs(currentRotation.x - toRotation.x) > 1.0E-4F
			|| Math.abs(currentRotation.y - toRotation.y) > 1.0E-4F
			|| Math.abs(currentRotation.z - toRotation.z) > 1.0E-4F
			|| Math.abs(currentRotation.w - toRotation.w) > 1.0E-4F;
	}

	public static boolean isNonDefault() {
		return lastTargetGravity != Direction.DOWN || isTransitioning();
	}
}
