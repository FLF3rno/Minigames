package net.mcreator.minigames.procedures;

import net.mcreator.minigames.init.MinigamesModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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

			entity.setDeltaMovement(
					entity.getDeltaMovement().x() * 1.1,
					0.42D,
					entity.getDeltaMovement().z() * 1.1
			);
		}
	}
}