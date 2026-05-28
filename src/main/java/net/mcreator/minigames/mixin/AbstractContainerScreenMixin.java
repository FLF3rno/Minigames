package net.mcreator.minigames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void openPauseMenuOnEsc(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (keyCode != 256) {
            return;
        }

        Screen currentScreen = (Screen) (Object) this;
        boolean isMinigamesScreen = currentScreen.getClass().getName().startsWith("net.mcreator.minigames.client.gui.");
        if (!isMinigamesScreen) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new PauseScreen(mc.hasSingleplayerServer()));
        cir.setReturnValue(true);
    }
}
