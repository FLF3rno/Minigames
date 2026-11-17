package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class SpectativePlayerRenderMixin {

    @Inject(method = "broadcastToPlayer", at = @At("HEAD"), cancellable = true)
    private void minigames$preventBroadcast(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        if (player != serverPlayer && serverPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative)
        {
            cir.setReturnValue(false);
        }
    }
}
