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

@Mixin(Entity.class)
public abstract class SpectativeEntityMixin {

    @Unique
    private boolean minigames$isSpectativePlayer() {
        return (Object) this instanceof Player player
                && player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative;
    }

    @Inject(method = "isIgnoringBlockTriggers", at = @At("HEAD"), cancellable = true)
    private void minigames$ignoreBlockTriggersWhenSpectative(CallbackInfoReturnable<Boolean> cir) {
        if (minigames$isSpectativePlayer()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isAffectedByBlocks", at = @At("HEAD"), cancellable = true)
    private void minigames$notAffectedByBlocksWhenSpectative(CallbackInfoReturnable<Boolean> cir) {
        if (minigames$isSpectativePlayer()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void minigames$noSprintParticleCheckWhenSpectative(CallbackInfoReturnable<Boolean> cir) {
        if (minigames$isSpectativePlayer()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "spawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void minigames$noSprintParticleWhenSpectative(CallbackInfo ci) {
        if (minigames$isSpectativePlayer()) {
            ci.cancel();
        }
    }

    @Inject(method = "playMuffledStepSound", at = @At("HEAD"), cancellable = true)
    private void minigames$noMuffledStepSoundWhenSpectative(BlockState blockState, BlockPos pos, CallbackInfo ci) {
        if (minigames$isSpectativePlayer()) {
            ci.cancel();
        }
    }
}
