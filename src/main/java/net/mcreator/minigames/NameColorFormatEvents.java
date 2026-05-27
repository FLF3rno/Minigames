//package net.mcreator.minigames;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.MinigamesModVariables;

//@EventBusSubscriber(modid = MinigamesMod.MODID)
//public class NameColorFormatEvents {
//	@SubscribeEvent
//	public static void onNameFormat(PlayerEvent.NameFormat event) {
//		Component colored = coloredName(event.getEntity());
//		if (colored != null)
//			event.setDisplayname(colored);
//	}
//
//	@SubscribeEvent
//	public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
//		Component colored = coloredName(event.getEntity());
//		if (colored != null)
//			event.setDisplayName(colored);
//	}
//
//	private static Component coloredName(Player player) {
//		if (player == null)
//			return null;
//		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
//		if (vars == null || vars.color == null || vars.color.isEmpty())
//			return null;
//		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
//		TextColor parsedColor = TextColor.parseColor(hex).result().orElse(null);
//		if (parsedColor == null)
//			return null;
//		return Component.literal(player.getGameProfile().getName()).setStyle(Style.EMPTY.withColor(parsedColor));
//	}
//}
