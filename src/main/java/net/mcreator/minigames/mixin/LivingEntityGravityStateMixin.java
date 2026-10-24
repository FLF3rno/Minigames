package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(LivingEntity.class)
public abstract class LivingEntityGravityStateMixin {
	@Unique
	private Direction minigames_lastGravity = Direction.DOWN;
	@Unique
	private boolean minigames_initialized = false;

	@Inject(method = "tick", at = @At("TAIL"))
	private void minigames$applyGravityState(CallbackInfo ci) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = player.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity;
		if (gravity == null) gravity = Direction.DOWN;

		if (!minigames_initialized) {
			minigames_initialized = true;
			minigames_lastGravity = gravity;
			return;
		}

		if (gravity != minigames_lastGravity) {
			Direction prev = minigames_lastGravity;
			minigames_lastGravity = gravity;
			if (player.level().isClientSide()) {
				net.minecraft.world.phys.Vec3 safePos = net.mcreator.minigames.gravity.GravityOrientation.calculateSafePosition(player, prev, gravity);
				player.setPos(safePos.x, safePos.y, safePos.z);
			}
			player.refreshDimensions();
		}

		if (gravity != Direction.DOWN) {
			player.fallDistance = 0.0F;
		}
	}
}
