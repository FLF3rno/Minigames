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
			String swappedColor = AccountSwapClient.getSwappedColor(info.getProfile().id());
			int colorRgb = net.mcreator.minigames.util.ColorUtils.parseHex(swappedColor);
			net.minecraft.network.chat.Style style = (original != null) ? original.getStyle() : net.minecraft.network.chat.Style.EMPTY;
			if (colorRgb != -1) {
				style = style.withColor(colorRgb);
			}
			cir.setReturnValue(Component.literal(swapped).setStyle(style));
		}
	}
}
