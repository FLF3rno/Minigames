package net.mcreator.minigames.procedures;

import net.mcreator.minigames.init.MinigamesModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

@EventBusSubscriber
public class KeybindPressedProcedure {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Entity entity = event.getEntity();

		if (entity == null) {
			return;
		}

		if (!entity.level().isClientSide()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();

		if (mc.player == null || !entity.equals(mc.player)) {
			return;
		}

		if (!CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.AIR_BOOTS.get()))) {
			return;
		}

		boolean jumpDown = mc.options.keyJump.isDown();

		if (entity.onGround()) {
			entity.getPersistentData().putInt("doubleJumpsUsed", 0);

			entity.getPersistentData().putBoolean("jumpWasDown", jumpDown);

			return;
		}

		int doubleJumpsUsed = entity.getPersistentData()
				.getInt("doubleJumpsUsed")
				.orElse(0);

		boolean jumpWasDown = entity.getPersistentData()
				.getBooleanOr("jumpWasDown", false);

		// Detect a NEW press, rather than a held key
		boolean newJumpPress = jumpDown && !jumpWasDown;

		// Save current key state
		entity.getPersistentData().putBoolean("jumpWasDown", jumpDown);

		if (doubleJumpsUsed >= 2) {
			return;
		}

		if (newJumpPress) {

			entity.getPersistentData().putInt(
					"doubleJumpsUsed",
					doubleJumpsUsed + 1
			);

			Direction gravity = GravityAccess.getGravity(entity);
			if (gravity == null) gravity = Direction.DOWN;
			Vec3 vel = entity.getDeltaMovement();

			if (gravity == Direction.DOWN) {
				entity.setDeltaMovement(
						vel.x * 1.1,
						0.42D,
						vel.z * 1.1
				);
			} else {
				Vec3 up = GravityOrientation.getUpVector(gravity);
				Vec3 down = GravityOrientation.getDownVector(gravity);
				Vec3 perp = vel.subtract(down.scale(vel.dot(down)));
				Vec3 jumpVel = perp.scale(1.1).add(up.scale(0.42D));
				entity.setDeltaMovement(jumpVel);
			}
		}
	}
}