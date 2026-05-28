package net.mcreator.minigames.event;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

@EventBusSubscriber(modid = "minigames")
public class ChatColorHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChat(ServerChatEvent event) {
        Player player = event.getPlayer();
        MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

        int rgb = ColorUtils.parseHex(vars.color);
        if (rgb != -1) {
            MutableComponent coloredName = Component.literal(player.getGameProfile().getName())
                    .withStyle(style -> style.withColor(rgb));
            
            Component newMessage = Component.literal("<")
                    .append(coloredName)
                    .append(Component.literal("> "))
                    .append(event.getMessage());

            event.setMessage(newMessage);
        }
    }
}