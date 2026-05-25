package net.mcreator.minigames;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityMath;
import net.mcreator.minigames.gravity.GravitySupport;
import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber
public class ChangeGravity {
	private static final double GRAVITY_ACCEL = 0.08D;
	private static final double TERMINAL_SPEED = 3.2D;
	private static final double DRAG = 0.995D;

	private ChangeGravity() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Entity entity = event.getEntity();
		Direction gravityDir = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity;
		boolean enabled = gravityDir != Direction.DOWN;
		execute(entity, gravityDir, enabled);
	}

	public static void execute(Entity entity, Direction gravityDir, boolean enabled) {
		if (!(entity instanceof LivingEntity living)) {
			return;
		}

		if (!enabled || gravityDir == null) {
			living.setNoGravity(false);
			return;
		}

		living.setNoGravity(true);

		Vec3 delta = living.getDeltaMovement();
		Vec3 gravity = GravityMath.down(gravityDir).scale(GRAVITY_ACCEL);
		Vec3 next = delta.add(gravity);

		double x = next.x;
		double y = next.y;
		double z = next.z;

		int sx = gravityDir.getStepX();
		int sy = gravityDir.getStepY();
		int sz = gravityDir.getStepZ();

		if (sx != 0) x = clampAxis(x, sx, TERMINAL_SPEED);
		if (sy != 0) y = clampAxis(y, sy, TERMINAL_SPEED);
		if (sz != 0) z = clampAxis(z, sz, TERMINAL_SPEED);

		x *= DRAG;
		y *= DRAG;
		z *= DRAG;

		living.setDeltaMovement(x, y, z);
		if (GravitySupport.isSupportedInGravity(living, gravityDir)) {
			living.setOnGround(true);
			living.fallDistance = 0.0F;
		}
		living.hurtMarked = true;
	}

	private static double clampAxis(double value, int sign, double maxAbs) {
		return sign > 0 ? Math.min(value, maxAbs) : Math.max(value, -maxAbs);
	}
}
