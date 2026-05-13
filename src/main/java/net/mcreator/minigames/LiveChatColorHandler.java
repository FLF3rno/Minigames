package net.mcreator.minigames;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(modid = MinigamesMod.MODID)
public class LiveChatColorHandler {
	@SubscribeEvent
	public static void onServerChat(ServerChatEvent event) {
		ServerPlayer player = event.getPlayer();
		if (player == null)
			return;
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars == null || vars.color == null || vars.color.isEmpty())
			return;
		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		TextColor parsed = TextColor.parseColor(hex).result().orElse(null);
		if (parsed == null)
			return;
		Component name = Component.literal(player.getGameProfile().getName()).setStyle(Style.EMPTY.withColor(parsed));
		Component message = Component.literal(event.getRawText());
		Component full = Component.empty().append(Component.literal("<")).append(name).append(Component.literal("> ")).append(message);
		event.setCanceled(true);
		if (player.getServer() != null) {
			player.getServer().getPlayerList().broadcastSystemMessage(full, false);
		}
	}
}
