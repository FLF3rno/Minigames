package net.mcreator.minigames.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.network.MinigamesModVariables;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBarRenderer.class)
public abstract class ExperienceBarRendererMixin {

    private static final Identifier BLUE_BACKGROUND =
            Identifier.fromNamespaceAndPath(
                    MinigamesMod.MODID,
                    "hud/experience_bar_background_blue"
            );

    private static final Identifier BLUE_PROGRESS =
            Identifier.fromNamespaceAndPath(
                    MinigamesMod.MODID,
                    "hud/experience_bar_progress_blue"
            );

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void minigames$blueXP(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker,
            CallbackInfo ci
    ) {

        LocalPlayer player = minecraft.player;

        if (player == null)
            return;

        if (!MinigamesModVariables.MapVariables.get(minecraft.level).playingDungeons)
            return;

        int xpNeeded = player.getXpNeededForNextLevel();

        if (xpNeeded <= 0) {
            ci.cancel();
            return;
        }

        int progress = (int)(player.experienceProgress * 183.0F);
        int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;
        int top = minecraft.getWindow().getGuiScaledHeight() - 32 + 3;


        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                BLUE_BACKGROUND,
                left,
                top,
                182,
                5
        );

        if (progress > 0) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    BLUE_PROGRESS,
                    182,
                    5,
                    0,
                    0,
                    left,
                    top,
                    progress,
                    5
            );
        }

        ci.cancel();
    }
}