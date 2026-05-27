package net.mcreator.minigames.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.Gui.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderTabListOnTop(GuiGraphics p_282884_, DeltaTracker p_348630_, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.keyPlayerList.isDown() && mc.level != null) {
            var tabList = mc.gui.getTabList();
            var scoreboard = mc.level.getScoreboard();
            var objective = scoreboard.getDisplayObjective(net.minecraft.world.scores.DisplaySlot.LIST);

            tabList.render(p_282884_, p_282884_.guiWidth(), scoreboard, objective);
        }
    }
}