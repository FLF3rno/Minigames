package net.mcreator.minigames.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderTabListOnTop(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = (Screen) (Object) this;
        boolean isMinigamesScreen = currentScreen.getClass().getName().startsWith("net.mcreator.minigames.client.gui.");

        if (isMinigamesScreen && mc.options.keyPlayerList.isDown() && mc.level != null) {
            var tabList = mc.gui.getTabList();
            var scoreboard = mc.level.getScoreboard();
            var objective = scoreboard.getDisplayObjective(net.minecraft.world.scores.DisplaySlot.LIST);

            tabList.render(guiGraphics, guiGraphics.guiWidth(), scoreboard, objective);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void allowTabListKeyThrough(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = (Screen) (Object) this;
        boolean isMinigamesScreen = currentScreen.getClass().getName().startsWith("net.mcreator.minigames.client.gui.");

        if (isMinigamesScreen && keyCode == 256) {
            mc.setScreen(new PauseScreen(mc.hasSingleplayerServer()));
            cir.setReturnValue(true);
            return;
        }

        if (isMinigamesScreen && mc.options.keyPlayerList.matches(keyCode, scanCode)) {
            cir.setReturnValue(false);
        }
    }
}
