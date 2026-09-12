package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.PhantomRenderState;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.network.PhantomStateSyncMessage;

@Mixin(EntityRenderer.class)
public abstract class PhantomEntityRendererMixin {

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void minigames(Entity entity, EntityRenderState state, float partialTicks, CallbackInfo ci) {
		if (state instanceof PhantomRenderState phantomState) {
			boolean isPhantom = (entity instanceof LivingEntity living && living.hasEffect(MinigamesModMobEffects.PHANTOM))
					|| PhantomStateSyncMessage.isPhantomOnClient(entity.getId());
			phantomState.minigames_setPhantom(isPhantom);
		}
	}
}