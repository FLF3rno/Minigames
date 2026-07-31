package net.mcreator.minigames;

import net.mcreator.minigames.network.StopMinigameMessage;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
@EventBusSubscriber(value = Dist.CLIENT)
public class PauseMenuButtonHandler {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof PauseScreen)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getConnection() == null
                || !hasEntityPermissionLevel(minecraft.player)) {
            return;
        }

        Button bottomButton = null;

        for (GuiEventListener listener : event.getListenersList()) {
            if (listener instanceof Button button) {
                if (button.getWidth() == 204) {
                    if (bottomButton == null || button.getY() > bottomButton.getY()) {
                        bottomButton = button;
                    }
                }
            }
        }

        if (bottomButton == null) {
            return;
        }

        int x = bottomButton.getX();
        int y = bottomButton.getY() + bottomButton.getHeight() + 4;

        Button minigameButton = Button.builder(
                        Component.translatable("gui.minigames.force_end"),

                button -> {
                    Player entity = minecraft.player;
                    Level world = minecraft.level;

                    if (entity == null || world == null) {
                        return;
                    }

                    double xx = entity.getX();
                    double yy = entity.getY();
                    double z = entity.getZ();
                        ClientPacketDistributor.sendToServer(new StopMinigameMessage(""));

                })
                .bounds(x, y, 204, 20)
                .build();

        event.addListener(minigameButton);
    }
    private static boolean hasEntityPermissionLevel(Entity entity) {
        if (entity instanceof Player _player) {
            return _player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
        }
        return false;
    }

}