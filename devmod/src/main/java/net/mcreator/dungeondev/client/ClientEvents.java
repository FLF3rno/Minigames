package net.mcreator.dungeondev.client;

import net.mcreator.dungeondev.DungeonDevMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = DungeonDevMod.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        var screen = event.getScreen();
        Minecraft mc = Minecraft.getInstance();

        if (screen instanceof TitleScreen titleScreen) {
            // Add prominent button in top-left of TitleScreen
            Button devWorldBtn = Button.builder(
                Component.literal("§6⚔ Dungeon Rooms"),
                b -> DevWorldHelper.openOrCreateDevWorld(mc, titleScreen)
            )
            .bounds(10, 10, 140, 20)
            .tooltip(Tooltip.create(Component.literal("Open or create empty void world with all dungeon_ rooms & structure blocks")))
            .build();

            event.addListener(devWorldBtn);
        } else if (screen instanceof SelectWorldScreen selectWorldScreen) {
            // Add button in top-right of SelectWorldScreen
            Button devWorldBtn = Button.builder(
                Component.literal("§6⚔ Dungeon Rooms"),
                b -> DevWorldHelper.openOrCreateDevWorld(mc, selectWorldScreen)
            )
            .bounds(selectWorldScreen.width - 150, 10, 140, 20)
            .tooltip(Tooltip.create(Component.literal("Open or create empty void world with all dungeon_ rooms & structure blocks")))
            .build();

            event.addListener(devWorldBtn);
        }
    }
}
