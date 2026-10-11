package net.mcreator.minigames.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.mcreator.minigames.event.FoodHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

	@Shadow
	private int foodLevel;

	@Shadow
	private float saturationLevel;

	@Shadow
	private float exhaustionLevel;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void minigames$preventRoguelikeFoodTick(ServerPlayer player, CallbackInfo ci) {
		if (player != null && FoodHandler.isEligible(player.level())) {
			if (this.foodLevel < 20) {
				this.foodLevel = 20;
			}
			if (this.saturationLevel < 20.0F) {
				this.saturationLevel = 20.0F;
			}
			this.exhaustionLevel = 0.0F;
			ci.cancel();
		}
	}
}
