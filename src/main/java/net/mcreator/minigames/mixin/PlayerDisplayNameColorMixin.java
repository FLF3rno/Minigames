package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(Player.class)
public abstract class PlayerDisplayNameColorMixin {
	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	private void minigames$applyCustomHexName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player) (Object) this;
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || !vars.showCustomNameColor || vars.color == null || vars.color.isEmpty())
			return;
		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		TextColor parsedColor = TextColor.parseColor(hex).result().orElse(null);
		if (parsedColor == null)
			return;
		cir.setReturnValue(Component.literal(player.getGameProfile().getName()).setStyle(Style.EMPTY.withColor(parsedColor)));
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	private void minigames$applyCustomHexDisplayName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player) (Object) this;
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || !vars.showCustomNameColor || vars.color == null || vars.color.isEmpty())
			return;
		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		TextColor parsedColor = TextColor.parseColor(hex).result().orElse(null);
		if (parsedColor == null)
			return;
		cir.setReturnValue(Component.literal(player.getName().getString()).setStyle(Style.EMPTY.withColor(parsedColor)));
	}
}
