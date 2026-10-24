package net.mcreator.minigames.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.AccountSwapManager;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

	@Inject(method = "getNameForDisplay", at = @At("HEAD"), cancellable = true)
	private void minigames$swappedTabDisplayName(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
		if (info == null || info.getProfile() == null) {
			return;
		}
		String swapped = AccountSwapManager.getSwappedName(info.getProfile().id());
		if (swapped == null || swapped.isEmpty()) {
			swapped = net.mcreator.minigames.client.AccountSwapClient.getSwappedName(info.getProfile().id());
		}
		if (swapped != null && !swapped.isEmpty()) {
			String swappedColor = AccountSwapManager.getSwappedColor(info.getProfile().id());
			if (swappedColor == null || swappedColor.isEmpty()) {
				swappedColor = net.mcreator.minigames.client.AccountSwapClient.getSwappedColor(info.getProfile().id());
			}
			int colorRgb = net.mcreator.minigames.util.ColorUtils.parseHex(swappedColor);

			Component tabName = info.getTabListDisplayName();
			if (tabName != null && tabName.getString().contains(swapped)) {
				MutableComponent result = tabName.copy();
				if (colorRgb != -1) {
					result = result.withStyle(style -> style.withColor(colorRgb));
				}
				cir.setReturnValue(minigames$decorate(info, result));
			} else if (tabName != null) {
				net.minecraft.network.chat.Style style = (colorRgb != -1) ? tabName.getStyle().withColor(colorRgb) : tabName.getStyle();
				MutableComponent swappedComp = Component.literal(swapped).setStyle(style);
				MutableComponent base = PlayerTeam.formatNameForTeam(info.getTeam(), swappedComp);
				cir.setReturnValue(minigames$decorate(info, base));
			} else {
				MutableComponent swappedComp = Component.literal(swapped);
				if (colorRgb != -1) {
					swappedComp = swappedComp.withStyle(style -> style.withColor(colorRgb));
				}
				MutableComponent base = PlayerTeam.formatNameForTeam(info.getTeam(), swappedComp);
				cir.setReturnValue(minigames$decorate(info, base));
			}
		}
	}

	private static Component minigames$decorate(PlayerInfo info, MutableComponent name) {
		if (info.getGameMode() == net.minecraft.world.level.GameType.SPECTATOR) {
			return name.withStyle(net.minecraft.ChatFormatting.ITALIC);
		}
		return name;
	}
}
