package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(LivingEntity.class)
public abstract class LivingEntityGravityStateMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void minigames$applyGravityState(CallbackInfo ci) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = player.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity;
		if (gravity != Direction.DOWN) {
			player.fallDistance = 0.0F;
			player.setNoGravity(true);
		}
	}
}
