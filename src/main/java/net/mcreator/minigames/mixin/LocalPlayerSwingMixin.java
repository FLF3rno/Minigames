package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;

import net.mcreator.minigames.client.DungeonWeaponClickBlocker;
import net.mcreator.minigames.client.ItemSizeHelper;
import net.mcreator.minigames.procedures.BoxingMasteryHandler;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerSwingMixin {
	@Inject(method = "swing", at = @At("HEAD"), cancellable = true)
	private void minigames$onSwing(InteractionHand hand, CallbackInfo ci) {
		if (hand == InteractionHand.MAIN_HAND) {
			LocalPlayer player = (LocalPlayer) (Object) this;
			if (!ItemSizeHelper.isInRoguelike(player.level())) {
				DungeonWeaponClickBlocker.suppressNextSwing = false;
				return;
			}
			if (DungeonWeaponClickBlocker.suppressNextSwing || BoxingMasteryHandler.isBoxing(player)) {
				DungeonWeaponClickBlocker.suppressNextSwing = false;
				ci.cancel();
			}
		}
	}
}
