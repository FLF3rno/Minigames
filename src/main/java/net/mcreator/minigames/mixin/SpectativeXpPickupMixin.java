package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class SpectativeXpPickupMixin {

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void minigames$preventXpPickupWhenSpectative(Player player, CallbackInfo ci) {
        if (player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "followNearbyPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
    )
    private boolean minigames$ignoreSpectativeForXpTargeting(Player player) {
        return player.isSpectator() || player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative;
    }
}
