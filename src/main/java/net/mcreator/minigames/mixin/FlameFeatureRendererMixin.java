package net.mcreator.minigames.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minigames.CustomFireHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlameFeatureRenderer.class)
public abstract class FlameFeatureRendererMixin {

	@Inject(method = "renderFlame", at = @At("HEAD"), cancellable = true)
	private void renderCustomFlame(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState state, Quaternionf rotation, AtlasManager atlasManager, CallbackInfo ci) {
		Integer color = state.getRenderData(CustomFireHelper.FIRE_COLOR_KEY);
		if (color == null || color == 0) {
			color = CustomFireHelper.findEntityFireColorAt(state.x, state.y, state.z);
		}
		if (color != null && color != 0) {
			CustomFireHelper.renderCustomEntityFlame(pose, bufferSource, state, rotation, atlasManager, color);
			ci.cancel();
		}
	}
}
