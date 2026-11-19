package net.mcreator.minigames.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.minigames.CustomFireHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlameFeatureRenderer.class)
public abstract class FlameFeatureRendererMixin {

	@Inject(method = "prepare", at = @At("HEAD"), cancellable = true)
	private void renderCustomFlame(FlameFeatureRenderer.Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2, CallbackInfo ci) {
		EntityRenderState state = submit.entityRenderState();
		Integer color = state.getRenderData(CustomFireHelper.FIRE_COLOR_KEY);
		if (color != null && color != 0) {
			CustomFireHelper.renderCustomEntityFlame(submit.pose(), buffer, state, submit.rotation(), Minecraft.getInstance().getAtlasManager(), color);
			ci.cancel();
		}
	}
}
