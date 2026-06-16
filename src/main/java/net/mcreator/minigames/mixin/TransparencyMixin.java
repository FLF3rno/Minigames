package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.LivingEntityTransparencyDataAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntityRenderer.class)
public abstract class TransparencyMixin {
    @Shadow
    protected abstract void setupRotations(LivingEntityRenderState state, PoseStack poseStack, float ageInTicks, float bodyRot);

    @Shadow
    protected abstract void scale(LivingEntityRenderState state, PoseStack poseStack);

    private static float getTransparencyPercent(LivingEntityRenderState state) {
        LivingEntity entity = state.getRenderData(net.mcreator.minigames.init.MinigamesModRenderStateModifiers.LIVING_ENTITY);
        if (entity instanceof LivingEntityTransparencyDataAccessor accessor) {
            return Math.max(0.0f, Math.min(100.0f, accessor.minigames$getTransparency()));
        }
        return 0.0f;
    }

    @Inject(
            method = "getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void minigames$forceTranslucentLayer(LivingEntityRenderState state, boolean visible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        if (getTransparencyPercent(state) <= 0.0f) {
            return;
        }

        LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
        ResourceLocation texture = renderer.getTextureLocation(state);
        cir.setReturnValue(RenderType.entityTranslucent(texture));
    }

    @Inject(
            method = "getModelTint(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void minigames$applyTransparencyToTint(LivingEntityRenderState state, CallbackInfoReturnable<Integer> cir) {
        float transparencyPercent = getTransparencyPercent(state);
        if (transparencyPercent <= 0.0f) {
            return;
        }

        int originalTint = cir.getReturnValueI();
        float opacityMultiplier = 1.0f - (transparencyPercent / 100.0f);
        int alpha = ARGB.alpha(originalTint);
        int red = ARGB.red(originalTint);
        int green = ARGB.green(originalTint);
        int blue = ARGB.blue(originalTint);
        cir.setReturnValue(ARGB.color(Math.round(alpha * opacityMultiplier), red, green, blue));
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void minigames$renderTransparentEntity(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        float transparencyPercent = getTransparencyPercent(state);
        if (transparencyPercent <= 0.0f) {
            return;
        }

        LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
        EntityModel model = renderer.getModel();
        ResourceLocation texture = renderer.getTextureLocation(state);
        float opacityMultiplier = 1.0f - (transparencyPercent / 100.0f);
        int alpha = Math.round(255.0f * opacityMultiplier);
        int color = ARGB.color(alpha, 255, 255, 255);

        poseStack.pushPose();
        this.setupRotations(state, poseStack, state.ageInTicks, state.bodyRot);
        this.scale(state, poseStack);
        model.setupAnim(state);
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        model.renderToBuffer(poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0F), color);
        poseStack.popPose();
        ci.cancel();
    }
}
