package net.mcreator.minigames.mixin;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class PhantomRenderTypeMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

	@Shadow
	public abstract Identifier getTextureLocation(S state);

	@Inject(
		method = "getRenderType",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$forcePhantomTranslucentRenderType(
		S state,
		boolean isBodyVisible,
		boolean forceTransparent,
		boolean appearGlowing,
		CallbackInfoReturnable<RenderType> cir
	) {
		if (net.mcreator.minigames.client.gui.ClassSelectionRoguelikeScreen.renderingSkinOverride != null) {
			cir.setReturnValue(RenderTypes.entityCutout(net.mcreator.minigames.client.gui.ClassSelectionRoguelikeScreen.renderingSkinOverride));
			return;
		}
		LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		if (entity != null && entity.hasEffect(MinigamesModMobEffects.PHANTOM)) {
			Identifier texture = this.getTextureLocation(state);
			cir.setReturnValue(RenderTypes.entityTranslucentCullItemTarget(texture));
		}
	}

	@Inject(
		method = "getModelTint",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$applyPhantomModelTint(
		S state,
		CallbackInfoReturnable<Integer> cir
	) {
		LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		if (entity != null && entity.hasEffect(MinigamesModMobEffects.PHANTOM)) {
			// 654311423 is the exact color Minecraft uses for forceTransparent (spectator/translucent body)
			// ARGB 0x27000000 or similar transparent alpha
			cir.setReturnValue(654311423);
		}
	}
}