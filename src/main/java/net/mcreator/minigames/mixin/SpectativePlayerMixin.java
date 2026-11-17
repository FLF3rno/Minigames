package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class SpectativePlayerMixin {

    @Unique
    private static boolean minigames$isSpectative(Player player) {
        return player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative;
    }

    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    private void minigames$notPickableWhenSpectative(CallbackInfoReturnable<Boolean> cir) {
        if (minigames$isSpectative((Player) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getMovementEmission", at = @At("HEAD"), cancellable = true)
    private void minigames$noMovementEmissionWhenSpectative(CallbackInfoReturnable<Entity.MovementEmission> cir) {
        if (minigames$isSpectative((Player) (Object) this)) {
            cir.setReturnValue(Entity.MovementEmission.NONE);
        }
    }

    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void minigames$noStepSoundWhenSpectative(BlockPos onPos, BlockState onState, CallbackInfo ci) {
        if (minigames$isSpectative((Player) (Object) this)) {
            ci.cancel();
        }
    }

    @Inject(method = "doWaterSplashEffect", at = @At("HEAD"), cancellable = true)
    private void minigames$noWaterSplashWhenSpectative(CallbackInfo ci) {
        if (minigames$isSpectative((Player) (Object) this)) {
            ci.cancel();
        }
    }
}
