package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerNameColorMixin {
	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	private void minigames$colorName(CallbackInfoReturnable<Component> cir) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		Component colored = coloredName(player);
		if (colored != null)
			cir.setReturnValue(colored);
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	private void minigames$colorDisplayName(CallbackInfoReturnable<Component> cir) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		Component colored = coloredName(player);
		if (colored != null)
			cir.setReturnValue(colored);
	}

	@Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
	private void minigames$colorTabListName(CallbackInfoReturnable<Component> cir) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		Component colored = coloredName(player);
		if (colored != null)
			cir.setReturnValue(colored);
	}

	private Component coloredName(ServerPlayer player) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || !vars.showCustomNameColor || vars.color == null || vars.color.isEmpty())
			return null;
		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		TextColor parsed = TextColor.parseColor(hex).result().orElse(null);
		if (parsed == null)
			return null;
		MutableComponent base = Component.literal(player.getGameProfile().getName());
		return base.setStyle(Style.EMPTY.withColor(parsed));
	}
}
