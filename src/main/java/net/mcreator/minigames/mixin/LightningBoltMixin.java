package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void minigames$skipSilentLightningSound(CallbackInfo ci) {
        LightningBolt lightning = (LightningBolt) (Object) this;

        if (lightning.isSilent()) {
            ci.cancel();
        }
    }
}