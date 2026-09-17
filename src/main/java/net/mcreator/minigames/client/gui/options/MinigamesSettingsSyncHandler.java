package net.mcreator.minigames.client.gui.options;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

@EventBusSubscriber
public class MinigamesSettingsSyncHandler {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		if (player != null && player.level() != null) {
			MinigamesSettingsHelper.syncToWorld(player.level(), player);
		}
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ClientSync {
		private static UUID lastPlayerId = null;

		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			if (player == null) {
				lastPlayerId = null;
				return;
			}
			UUID currentId = player.getUUID();
			if (lastPlayerId == null || !lastPlayerId.equals(currentId)) {
				lastPlayerId = currentId;
				if (mc.level != null) {
					MinigamesSettingsHelper.syncToWorld(mc.level, player);
					MinigamesSettingsHelper.syncClientToServer();
				}
			}
		}
	}
}
