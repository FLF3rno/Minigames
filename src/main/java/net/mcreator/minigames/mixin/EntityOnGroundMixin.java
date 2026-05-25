package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravitySupport;

@Mixin(Entity.class)
public abstract class EntityOnGroundMixin {
	@Inject(method = "onGround", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityAwareOnGround(CallbackInfoReturnable<Boolean> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;
		cir.setReturnValue(GravitySupport.isSupportedInGravity(player, gravity));
	}
}
