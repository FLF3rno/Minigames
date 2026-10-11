package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.GoldenRenderState;
import net.mcreator.minigames.client.GoldenTextureManager;
import net.mcreator.minigames.client.ObsidianTextureManager;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.GoldenStateSyncMessage;
import net.mcreator.minigames.network.ObsidianStateSyncMessage;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class GoldenRenderMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

	@Shadow
	protected M model;

	@Shadow
	public abstract Identifier getTextureLocation(S state);

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void minigames_extractGolden(T entity, S state, float partialTicks, CallbackInfo ci) {
		if (state instanceof GoldenRenderState goldenState) {
			boolean isGolden = entity != null && (entity.hasEffect(MinigamesModMobEffects.GOLDEN) || GoldenStateSyncMessage.isGoldenOnClient(entity.getId()));
			goldenState.minigames_setGolden(isGolden);
			boolean isObsidian = entity != null && (entity.hasEffect(MinigamesModMobEffects.OBSIDIANIFICATION) || ObsidianStateSyncMessage.isObsidianOnClient(entity.getId()));
			goldenState.minigames_setObsidian(isObsidian);
		}
	}

	@Inject(
		method = "getRenderType",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames_getGoldenRenderType(
		S state,
		boolean isBodyVisible,
		boolean forceTransparent,
		boolean appearGlowing,
		CallbackInfoReturnable<RenderType> cir
	) {
		if (state instanceof GoldenRenderState goldenState && !appearGlowing) {
			if (goldenState.minigames_isObsidian()) {
				Identifier original = this.getTextureLocation(state);
				Identifier obsidianTexture = ObsidianTextureManager.getObsidianTexture(original);
				// Solid obsidian with NO transparency - use entity cutout without culling instead of translucent
				cir.setReturnValue(RenderTypes.entityCutout(obsidianTexture, false));
			} else if (goldenState.minigames_isGolden()) {
				Identifier original = this.getTextureLocation(state);
				Identifier goldenTexture = GoldenTextureManager.getGoldenTexture(original);
				if (forceTransparent) {
					cir.setReturnValue(RenderTypes.entityTranslucentCullItemTarget(goldenTexture));
				} else if (isBodyVisible) {
					cir.setReturnValue(this.model.renderType(goldenTexture));
				}
			}
		}
	}

	@Inject(
		method = "isBodyVisible",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames_isBodyVisible(S state, CallbackInfoReturnable<Boolean> cir) {
		if (state instanceof GoldenRenderState goldenState && (goldenState.minigames_isGolden() || goldenState.minigames_isObsidian())) {
			cir.setReturnValue(true);
		}
	}

	@Inject(
		method = "shouldRenderLayers",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames_shouldRenderLayers(S state, CallbackInfoReturnable<Boolean> cir) {
		if (state instanceof GoldenRenderState goldenState && (goldenState.minigames_isObsidian() || goldenState.minigames_isGolden())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
		method = "getModelTint",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames_getGoldenModelTint(S state, CallbackInfoReturnable<Integer> cir) {
		if (state instanceof GoldenRenderState goldenState && (goldenState.minigames_isGolden() || goldenState.minigames_isObsidian())) {
			cir.setReturnValue(-1);
		}
	}
}
