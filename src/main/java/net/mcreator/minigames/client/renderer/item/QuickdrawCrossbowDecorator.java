package net.mcreator.minigames.client.renderer.item;

import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.item.QuickdrawCrossbowItem;

@EventBusSubscriber(Dist.CLIENT)
public class QuickdrawCrossbowDecorator implements IItemDecorator {
    private static final int COLOR_GOLD = 0xFFFFAA00;
    private static final int COLOR_WHITE = 0xFFFFFFFF;

    @SubscribeEvent
    public static void register(RegisterItemDecorationsEvent event) {
        event.register(MinigamesModItems.QUICKDRAW_CROSSBOW.get(), new QuickdrawCrossbowDecorator());
    }

    @Override
    public boolean render(GuiGraphicsExtractor guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        int loaded = QuickdrawCrossbowItem.getLoadedArrows(stack);
        if (loaded <= 0) {
            return false;
        }

        int max = QuickdrawCrossbowItem.getMaxArrows(stack);
        String text = String.valueOf(loaded);
        int color = (loaded >= max) ? COLOR_GOLD : COLOR_WHITE;

        guiGraphics.text(font, text, xOffset + 17 - font.width(text), yOffset + 9, color, true);
        return false;
    }
}
