package net.mcreator.minigames.mixin;

import net.mcreator.minigames.AccountSwapManager;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerNameColorMixin {

	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	private void minigames$applyDisplayNameColor(CallbackInfoReturnable<Component> cir) {
		minigames$applyColor(cir);
	}

	@Unique
	private void minigames$applyColor(CallbackInfoReturnable<Component> cir) {
		Player player = (Player) (Object) this;
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		Component base = cir.getReturnValue();
		if (swapped != null && !swapped.isEmpty() && base != null) {
			if (!base.getString().contains(swapped)) {
				String orig = player.getGameProfile().name();
				if (base.getString().equals(orig)) {
					base = Component.literal(swapped).setStyle(base.getStyle());
				} else if (player.getTeam() != null) {
					base = PlayerTeam.formatNameForTeam(player.getTeam(), Component.literal(swapped));
				} else {
					base = Component.literal(swapped);
				}
			}
		}

		String colorStr = AccountSwapManager.getSwappedColor(player.getUUID());
		if (colorStr == null || colorStr.isEmpty()) {
			MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
			colorStr = vars.color;
		}
		int rgb = ColorUtils.parseHex(colorStr);
		if (rgb != -1 && base != null) {
			MutableComponent colored = base.copy().withStyle(style -> style.withColor(rgb));
			cir.setReturnValue(colored);
		} else if (base != null && base != cir.getReturnValue()) {
			cir.setReturnValue(base);
		}
	}
}