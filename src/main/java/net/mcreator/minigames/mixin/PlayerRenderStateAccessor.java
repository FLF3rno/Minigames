package net.mcreator.minigames.mixin;

import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerRenderState.class)
public interface PlayerRenderStateAccessor {
    @Accessor("minigames$glowColor")
    String getAdvancedGlowingColor();
}