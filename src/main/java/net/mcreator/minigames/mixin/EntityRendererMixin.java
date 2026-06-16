package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.MinigamesModPlayerAnimationAPI;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
	private String master = null;
	private Minecraft mc = Minecraft.getInstance();

	@Inject(method = "affectedByCulling", at = @At("HEAD"), cancellable = true)
	private void affectedByCulling(T player, CallbackInfoReturnable<Boolean> cir) {
		if (master == null) {
			if (!MinigamesModPlayerAnimationAPI.animations.isEmpty())
				master = "minigames";
			else
				return;
		}
		if (!master.equals("minigames"))
			return;
		if (player instanceof Player plr && plr != mc.player && MinigamesModPlayerAnimationAPI.active_animations.get(plr) != null)
			cir.setReturnValue(false);
	}
}