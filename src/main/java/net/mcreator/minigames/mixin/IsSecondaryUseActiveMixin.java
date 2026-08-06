package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class IsSecondaryUseActiveMixin {
    @Inject(method = "isSecondaryUseActive", at = @At("HEAD"), cancellable = true)
    private void minigames$gravityAwareOnGround(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (!MinigamesModVariables.MapVariables.get(player.level()).playingDungeons) return;
        cir.setReturnValue(true);
    }
}
