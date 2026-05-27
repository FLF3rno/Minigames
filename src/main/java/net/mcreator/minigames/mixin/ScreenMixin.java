package net.mcreator.minigames.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTabListOnTop(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.keyPlayerList.isDown()) {
            var tabList = ((GuiAccessor) mc.gui).getTabList();
            assert mc.level != null;
            var scoreboard = mc.level.getScoreboard();
            var objective = scoreboard.getDisplayObjective(net.minecraft.world.scores.DisplaySlot.LIST);

            tabList.setVisible(true);
            tabList.render(guiGraphics, guiGraphics.guiWidth(), scoreboard, objective);
        }
    }
}