package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
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
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

		int rgb = ColorUtils.parseHex(vars.color);
		if (rgb != -1 && cir.getReturnValue() != null) {
			MutableComponent colored = cir.getReturnValue().copy().withStyle(style -> style.withColor(rgb));
			cir.setReturnValue(colored);
		}
	}
}