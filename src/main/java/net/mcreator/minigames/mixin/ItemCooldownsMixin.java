package net.mcreator.minigames.mixin;

import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {
	@Shadow
	private Map<Identifier, ?> cooldowns;

	@Shadow
	private int tickCount;

	@Shadow
	protected abstract void onCooldownStarted(Identifier group, int duration);

	@Inject(method = "addCooldown(Lnet/minecraft/resources/Identifier;I)V", at = @At("HEAD"), cancellable = true)
	private void minigames$preserveCooldownScale(Identifier group, int duration, CallbackInfo ci) {
		if (this.cooldowns != null && this.cooldowns.containsKey(group)) {
			Object existingObj = this.cooldowns.get(group);
			if (existingObj instanceof CooldownInstanceAccessor accessor) {
				int existingRemaining = accessor.getEndTime() - this.tickCount;
				if (existingRemaining > 0 && duration > 0) {
					int originalDuration = Math.max(accessor.getEndTime() - accessor.getStartTime(), duration);
					int newEndTime = this.tickCount + duration;
					int newStartTime = newEndTime - originalDuration;
					accessor.setStartTime(newStartTime);
					accessor.setEndTime(newEndTime);
					this.onCooldownStarted(group, duration);
					ci.cancel();
				}
			}
		}
	}
}
