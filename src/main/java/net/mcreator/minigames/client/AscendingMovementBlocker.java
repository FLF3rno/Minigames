package net.mcreator.minigames.client;

import java.lang.reflect.Field;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

import net.mcreator.minigames.init.MinigamesModMobEffects;

@EventBusSubscriber(value = Dist.CLIENT)
public final class AscendingMovementBlocker {
	private static Field moveVectorField;

	private AscendingMovementBlocker() {
	}

	@SubscribeEvent
	public static void onMovementInput(MovementInputUpdateEvent event) {
		if (!event.getEntity().hasEffect(MinigamesModMobEffects.ASCENDING)) {
			return;
		}

		ClientInput input = event.getInput();

		// Clear keys (forward/back/left/right/jump/sprint/shift).
		input.keyPresses = Input.EMPTY;

		// Also force the computed movement vector to zero (it's protected).
		try {
			if (moveVectorField == null) {
				moveVectorField = ClientInput.class.getDeclaredField("moveVector");
				moveVectorField.setAccessible(true);
			}
			moveVectorField.set(input, Vec2.ZERO);
		} catch (Throwable ignored) {
			// If reflection fails, keyPresses reset still blocks most movement.
		}
	}
}

