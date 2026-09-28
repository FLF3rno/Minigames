package net.mcreator.minigames;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.mcreator.minigames.util.ColorUtils;

@EventBusSubscriber(modid = MinigamesMod.MODID)
public class NameColorFormatEvents {
	@SubscribeEvent
	public static void onNameFormat(PlayerEvent.NameFormat event) {
		Player player = event.getEntity();
		if (player == null) {
			return;
		}
		String nameStr = AccountSwapManager.getSwappedName(player.getUUID());
		if (nameStr == null || nameStr.isEmpty()) {
			nameStr = player.getName().getString();
		}
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		int rgb = ColorUtils.parseHex(vars.color);
		if (rgb != -1 || AccountSwapManager.getSwappedName(player.getUUID()) != null) {
			net.minecraft.network.chat.MutableComponent comp = Component.literal(nameStr);
			if (rgb != -1) {
				comp = comp.withStyle(style -> style.withColor(rgb));
			}
			if (player.getTeam() != null) {
				event.setDisplayname(net.minecraft.world.scores.PlayerTeam.formatNameForTeam(player.getTeam(), comp));
			} else {
				event.setDisplayname(comp);
			}
		}
	}

	@SubscribeEvent
	public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
		Player player = event.getEntity();
		if (player == null) {
			return;
		}
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		if (swapped != null && !swapped.isEmpty()) {
			event.setDisplayName(Component.literal(swapped));
		}
		Component colored = coloredName(player);
		if (colored != null) {
			event.setDisplayName(colored);
		}
	}

	private static Component coloredName(Player player) {
		if (player == null) {
			return null;
		}

		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars.color == null || vars.color.isEmpty()) {
			return null;
		}

		String hex = vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		TextColor parsedColor = TextColor.parseColor(hex).result().orElse(null);
		if (parsedColor == null) {
			return null;
		}

		String nameStr = AccountSwapManager.getSwappedName(player.getUUID());
		if (nameStr == null || nameStr.isEmpty()) {
			nameStr = player.getName().getString();
		}

		Component coloredPlayerName = Component.literal(nameStr)
				.setStyle(Style.EMPTY.withColor(parsedColor));

		if (player.getTeam() != null) {
			Component prefix = player.getTeam().getPlayerPrefix();
			Component suffix = player.getTeam().getPlayerSuffix();

			return Component.empty()
					.append(prefix)
					.append(coloredPlayerName)
					.append(suffix);
		}

		return coloredPlayerName;
	}
}

