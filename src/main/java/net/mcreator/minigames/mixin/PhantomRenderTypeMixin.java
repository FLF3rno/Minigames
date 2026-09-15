package net.mcreator.minigames.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minigames.client.PhantomRenderState;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.PhantomStateSyncMessage;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class PhantomRenderTypeMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

	@Shadow
	public abstract Identifier getTextureLocation(S state);

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void minigames(T entity, S state, float partialTicks, CallbackInfo ci) {
		if (state instanceof PhantomRenderState phantomState) {
			boolean isPhantom = entity != null && (entity.hasEffect(MinigamesModMobEffects.PHANTOM)
					|| PhantomStateSyncMessage.isPhantomOnClient(entity.getId()));
			phantomState.minigames_setPhantom(isPhantom);
		}
	}

	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
		at = @At("HEAD")
	)
	private void minigames(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		if (state instanceof PhantomRenderState phantomState && phantomState.minigames_isPhantom()) {
			state.isInvisibleToPlayer = false;
		}
	}

	@Inject(
		method = "isBodyVisible",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames(S state, CallbackInfoReturnable<Boolean> cir) {
		if (state instanceof PhantomRenderState phantomState && phantomState.minigames_isPhantom()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
		method = "getRenderType",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames(
		S state,
		boolean isBodyVisible,
		boolean forceTransparent,
		boolean appearGlowing,
		CallbackInfoReturnable<RenderType> cir
	) {
		if (state instanceof PhantomRenderState phantomState && phantomState.minigames_isPhantom()) {
			Identifier texture = this.getTextureLocation(state);
			cir.setReturnValue(RenderTypes.entityTranslucentCullItemTarget(texture));
		}
	}
}
