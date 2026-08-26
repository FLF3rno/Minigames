package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.decoration.HangingEntity;
import net.mcreator.minigames.network.MinigamesModVariables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HangingEntity.class)
public abstract class HangingEntityMixin {
	@Inject(method = "survives", at = @At("HEAD"), cancellable = true)
	private void minigames$preventPaintingPopOff(CallbackInfoReturnable<Boolean> cir) {
		HangingEntity self = (HangingEntity) (Object) this;
		String className = self.getClass().getSimpleName();
		if (className.equals("Painting") || className.equals("PaintingEntity") || className.equals("EntityPainting")) {
			if (self.level() != null && MinigamesModVariables.MapVariables.get(self.level()).playingDungeons) {
				cir.setReturnValue(true);
			}
		}
	}
}
