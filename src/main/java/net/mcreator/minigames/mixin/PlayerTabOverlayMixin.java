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
			Component tabName = info.getTabListDisplayName();
			if (tabName != null && tabName.getString().contains(swapped)) {
				cir.setReturnValue(minigames$decorate(info, tabName.copy()));
			} else if (tabName != null) {
				MutableComponent swappedComp = Component.literal(swapped).setStyle(tabName.getStyle());
				MutableComponent base = PlayerTeam.formatNameForTeam(info.getTeam(), swappedComp);
				cir.setReturnValue(minigames$decorate(info, base));
			} else {
				MutableComponent base = PlayerTeam.formatNameForTeam(info.getTeam(), Component.literal(swapped));
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
