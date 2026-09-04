package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.mcreator.minigames.procedures.ApplyScreenshakeProcedure;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
            method = "extractCamera",
            at = @At("TAIL")
    )
    private void minigames$applyScreenshake(CallbackInfo ci) {
        double intensity = ApplyScreenshakeProcedure.getIntensity();

        if (intensity <= 0.0) {
            return;
        }

        GameRenderer renderer = (GameRenderer) (Object) this;
        CameraRenderState cameraState = renderer.getGameRenderState().levelRenderState.cameraRenderState;

        double time = System.currentTimeMillis() / 50.0;

        float pitch = (float)(Math.sin(time * 0.47) * intensity * 5.0);
        float yaw   = (float)(Math.sin(time * 0.71 + 1.3) * intensity * 5.0);
        float roll  = (float)(Math.sin(time * 0.91 + 2.7) * intensity * 5.0);

        cameraState.viewRotationMatrix.rotateX((float) Math.toRadians(pitch)).rotateY((float) Math.toRadians(yaw)).rotateZ((float) Math.toRadians(roll));
    }
}