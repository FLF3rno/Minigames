package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;

import net.mcreator.minigames.client.DungeonWeaponClickBlocker;
import net.mcreator.minigames.client.ItemSizeHelper;
import net.mcreator.minigames.procedures.BoxingMasteryHandler;

@Mixin(LivingEntity.class)
public abstract class LocalPlayerSwingMixin {
	@Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z", at = @At("HEAD"), cancellable = true)
	private void minigames$onSwing(InteractionHand hand, SwingAnimation animation, boolean sendToSwingingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (hand == InteractionHand.MAIN_HAND && (Object) this instanceof LocalPlayer player) {
			if (!ItemSizeHelper.isInRoguelike(player.level())) {
				DungeonWeaponClickBlocker.suppressNextSwing = false;
				return;
			}
			if (DungeonWeaponClickBlocker.suppressNextSwing || BoxingMasteryHandler.isBoxing(player)) {
				DungeonWeaponClickBlocker.suppressNextSwing = false;
				cir.setReturnValue(false);
			}
		}
	}
}
