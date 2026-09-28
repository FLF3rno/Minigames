package net.mcreator.minigames.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.client.AccountSwapClient;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

	@Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
	private void minigames$swappedPlayerInfoSkin(CallbackInfoReturnable<PlayerSkin> cir) {
		PlayerInfo info = (PlayerInfo) (Object) this;
		PlayerSkin skin = AccountSwapClient.getSkin(info.getProfile().id());
		if (skin != null) {
			cir.setReturnValue(skin);
		}
	}

	@Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
	private void minigames$swappedTabListDisplayName(CallbackInfoReturnable<Component> cir) {
		PlayerInfo info = (PlayerInfo) (Object) this;
		String swapped = AccountSwapClient.getSwappedName(info.getProfile().id());
		if (swapped != null && !swapped.isEmpty()) {
			Component original = cir.getReturnValue();
			if (original != null && original.getString().contains(swapped)) {
				return;
			}
			if (original != null) {
				cir.setReturnValue(Component.literal(swapped).setStyle(original.getStyle()));
			} else {
				cir.setReturnValue(Component.literal(swapped));
			}
		}
	}
}
