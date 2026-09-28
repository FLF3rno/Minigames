package net.mcreator.minigames.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.AccountSwapManager;

@Mixin(Player.class)
public class PlayerIdentityMixin {

	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	private void minigames$swappedName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player) (Object) this;
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		if (swapped != null && !swapped.isEmpty()) {
			cir.setReturnValue(Component.literal(swapped));
		}
	}

	@Inject(method = "getPlainTextName", at = @At("RETURN"), cancellable = true)
	private void minigames$swappedPlainTextName(CallbackInfoReturnable<String> cir) {
		Player player = (Player) (Object) this;
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		if (swapped != null && !swapped.isEmpty()) {
			cir.setReturnValue(swapped);
		}
	}
}
