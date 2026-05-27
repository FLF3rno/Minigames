package net.mcreator.minigames.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {
    @Accessor("tabList")
    PlayerTabOverlay getTabList();
}
