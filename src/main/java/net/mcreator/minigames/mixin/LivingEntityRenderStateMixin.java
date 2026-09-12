package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.PhantomRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements PhantomRenderState {

	@Unique
	private boolean minigames_phantom;

	@Override
	public boolean minigames_isPhantom() {
		return this.minigames_phantom;
	}

	@Override
	public void minigames_setPhantom(boolean phantom) {
		this.minigames_phantom = phantom;
	}
}
