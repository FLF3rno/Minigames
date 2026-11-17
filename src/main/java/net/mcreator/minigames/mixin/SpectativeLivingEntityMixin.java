package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class SpectativeLivingEntityMixin {

    @Unique
    private boolean minigames$isSpectativePlayer() {
        return (Object) this instanceof Player player
                && player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative;
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void minigames$notPushableWhenSpectative(CallbackInfoReturnable<Boolean> cir) {
        if (minigames$isSpectativePlayer()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "checkFallDamage", at = @At("HEAD"), cancellable = true)
    private void minigames$noFallParticlesOrDamageWhenSpectative(double ya, boolean onGround, BlockState onState, BlockPos pos, CallbackInfo ci) {
        if (minigames$isSpectativePlayer()) {
            if (onGround) {
                ((Entity) (Object) this).resetFallDistance();
            }
            ci.cancel();
        }
    }
}
