package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.OpenDungeonInventoryMessage;

@EventBusSubscriber(Dist.CLIENT)
public class DungeonInventoryRedirectHandler {
	@SubscribeEvent
	public static void onScreenOpening(ScreenEvent.Opening event) {
		if (!(event.getNewScreen() instanceof InventoryScreen))
			return;
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null)
			return;
		String classDungeon = minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		String normalizedClass = classDungeon == null ? "" : classDungeon.trim().toLowerCase();
		if ("warrior".equals(normalizedClass) || "support".equals(normalizedClass) || "thief".equals(normalizedClass) || "mage".equals(normalizedClass)) {
			event.setNewScreen(null);
			ClientPacketDistributor.sendToServer(new OpenDungeonInventoryMessage());
		}
	}
}
