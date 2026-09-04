package net.mcreator.minigames.mixin;

import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.LightCoordsUtil;
import net.mcreator.minigames.network.MinigamesModVariables;

@Mixin(LightCoordsUtil.class)
public class LightCoordsUtilMixin {

    @Unique
    private static Boolean shadersModPresent = null;

    @Inject(method = "pack(II)I", at = @At("HEAD"), cancellable = true)
    private static void onPack(int blockLight, int skyLight, CallbackInfoReturnable<Integer> cir) {
        try {
            if (areShadersEnabled()) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc == null || mc.player == null) {
                return;
            }

            var variables = mc.player.getData(MinigamesModVariables.PLAYER_VARIABLES);

            int minLight = Mth.clamp((int) variables.minimumLightLevel, 0, 15);

            int maxLight = Mth.clamp((int) variables.maximumLightLevel, 0, 15);

            if (minLight > maxLight) {
                minLight = maxLight;
            }

            int newBlockLight = Mth.clamp(blockLight, minLight, maxLight);

            int newSkyLight = Mth.clamp(skyLight, minLight, maxLight);

            cir.setReturnValue((newBlockLight << 4) | (newSkyLight << 20));

        } catch (Throwable t) {
        }
    }

    @Unique
    private static boolean areShadersEnabled() {
        if (shadersModPresent == null) {
            try {
                Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                shadersModPresent = true;
            } catch (ClassNotFoundException e) {
                shadersModPresent = false;
            }
        }
        if (!shadersModPresent) {
            return false;
        }
        try {
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Object instance = irisApiClass.getMethod("getInstance").invoke(null);
            return (boolean) irisApiClass.getMethod("isShaderPackInUse").invoke(instance);
        } catch (Throwable e) {
            return false;
        }
    }
}
