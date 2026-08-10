package net.mcreator.minigames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.init.MinigamesModMobEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class DecayHeartMixin {

    @Inject(
            method = "extractHeart",
            at = @At("HEAD"),
            cancellable = true
    )
    private void minigames$extractDecayHeart(
            GuiGraphicsExtractor graphics,
            Gui.HeartType type,
            int xo,
            int yo,
            boolean isHardcore,
            boolean blinks,
            boolean half,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        if (!minecraft.player.hasEffect(MinigamesModMobEffects.DECAY)) {
            return;
        }

        if (type == Gui.HeartType.CONTAINER ||
                type == Gui.HeartType.ABSORBING) {
            return;
        }

        Identifier sprite;

        if (isHardcore) {
            if (half) {
                sprite = blinks
                        ? Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_hardcore_half_blinking"
                )
                        : Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_hardcore_half"
                );
            } else {
                sprite = blinks
                        ? Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_hardcore_full_blinking"
                )
                        : Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_hardcore_full"
                );
            }
        } else {
            if (half) {
                sprite = blinks
                        ? Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_half_blinking"
                )
                        : Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_half"
                );
            } else {
                sprite = blinks
                        ? Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_full_blinking"
                )
                        : Identifier.fromNamespaceAndPath(
                        "minigames",
                        "hud/heart/decay_full"
                );
            }
        }

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                sprite,
                xo,
                yo,
                9,
                9
        );

        ci.cancel();
    }
}