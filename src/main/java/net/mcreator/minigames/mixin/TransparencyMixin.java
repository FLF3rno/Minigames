package net.mcreator.minigames.mixin;

import net.mcreator.minigames.util.CustomRenderModifiers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class TransparencyMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(
            method = "getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void minigames$forceTranslucentLayer(LivingEntityRenderState state, boolean visible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        Float rawNbtTransparency = state.getRenderData(CustomRenderModifiers.TRANSPARENCY);
        float nbtValue = (rawNbtTransparency != null) ? rawNbtTransparency : 0.0f;

        if (nbtValue > 0.0f && nbtValue <= 100.0f) {

            LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
            ResourceLocation texture = renderer.getTextureLocation(state);
            cir.setReturnValue(RenderType.entityTranslucent(texture));
        }
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
            ),
            ordinal = 0
    )
    private int minigames$modifyColor(int originalColor, LivingEntityRenderState state) {
        Float rawNbtTransparency = state.getRenderData(CustomRenderModifiers.TRANSPARENCY);
        float nbtValue = (rawNbtTransparency != null) ? rawNbtTransparency : 0.0f;

        float opacityMultiplier = 1.0f - (nbtValue / 100.0f);
        opacityMultiplier = Math.max(0.0f, Math.min(1.0f, opacityMultiplier));

        if (opacityMultiplier >= 1.0f) {
            return originalColor;
        }

        int alpha = ARGB.alpha(originalColor);
        int red = ARGB.red(originalColor);
        int green = ARGB.green(originalColor);
        int blue = ARGB.blue(originalColor);

        int newAlpha = (int) (alpha * opacityMultiplier);
        return ARGB.color(newAlpha, red, green, blue);
    }
}