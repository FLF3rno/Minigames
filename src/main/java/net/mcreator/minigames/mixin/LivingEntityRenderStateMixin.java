package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.PhantomRenderState;
import net.mcreator.minigames.client.GoldenRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements PhantomRenderState, GoldenRenderState {
	@Unique
	private boolean minigames_phantom;

	@Unique
	private boolean minigames_golden;

	@Override
	public boolean minigames_isPhantom() {
		return this.minigames_phantom;
	}

	@Override
	public void minigames_setPhantom(boolean phantom) {
		this.minigames_phantom = phantom;
	}

	@Override
	public boolean minigames_isGolden() {
		return this.minigames_golden;
	}

	@Override
	public void minigames_setGolden(boolean golden) {
		this.minigames_golden = golden;
	}
}
