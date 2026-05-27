package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(Player.class)
public abstract class PlayerTeamColorMixin {
	@Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
	private void minigames$customGlowTeamColorPlayer(CallbackInfoReturnable<Integer> cir) {
		Player player = (Player) (Object) this;
		if (!player.hasEffect(net.minecraft.world.effect.MobEffects.GLOWING)) {
			return;
		}

		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars != null && vars.color != null && !vars.color.isEmpty()) {
			int rgb = parseHex(vars.color, -1);
			if (rgb != -1) {
				cir.setReturnValue(rgb);
				return;
			}
		}

		TextColor displayColor = player.getDisplayName().getStyle().getColor();
		if (displayColor != null) {
			cir.setReturnValue(displayColor.getValue());
		}
	}

	private static int parseHex(String value, int fallback) {
		if (value == null) return fallback;
		String h = value.startsWith("#") ? value.substring(1) : value;
		if (!h.matches("^[0-9a-fA-F]{6}$")) return fallback;
		return Integer.parseInt(h, 16);
	}
}
