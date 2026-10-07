package net.mcreator.minigames.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.client.AccountSwapClient;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

	@Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
	private void minigames$swappedClientPlayerSkin(CallbackInfoReturnable<PlayerSkin> cir) {
		AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
		PlayerSkin skin = AccountSwapClient.getSkin(player.getUUID());
		if (skin != null) {
			cir.setReturnValue(skin);
		}
	}

	@Inject(method = "updateBob", at = @At("HEAD"), cancellable = true)
	private void minigames$gravityUpdateBob(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
		AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
		net.minecraft.core.Direction gravity = net.mcreator.minigames.gravity.GravityAccess.getGravity(player);
		if (gravity == net.minecraft.core.Direction.DOWN) return;

		float tBob;
		if (player.onGround() && !player.isDeadOrDying() && !player.isSwimming()) {
			net.minecraft.world.phys.Vec3 perp = net.mcreator.minigames.gravity.GravityOrientation.getPerpendicularComponent(player.getDeltaMovement(), gravity);
			tBob = Math.min(0.1F, (float) perp.length());
		} else {
			tBob = 0.0F;
		}

		player.avatarState().updateBob(tBob);
		ci.cancel();
	}
}
