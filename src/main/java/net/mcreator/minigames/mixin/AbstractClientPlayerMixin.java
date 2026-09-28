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
}
