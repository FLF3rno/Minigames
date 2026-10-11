package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.mcreator.minigames.event.FoodHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerFoodMixin {

	@Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
	private void minigames$preventRoguelikeExhaustion(float amount, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		Level level = player.level();
		if (FoodHandler.isEligible(level)) {
			ci.cancel();
		}
	}
}
