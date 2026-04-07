package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.network.NameColorPreferenceMessage;

import java.io.File;
import java.util.UUID;

@EventBusSubscriber(value = Dist.CLIENT)
public class NameColorPreferenceClient {
	private static boolean sentPreference = false;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null) {
			sentPreference = false;
			return;
		}
		if (sentPreference)
			return;
		String color = readClientColor(player.getUUID());
		if (color != null && !color.isEmpty()) {
			ClientPacketDistributor.sendToServer(new NameColorPreferenceMessage(color));
		}
		sentPreference = true;
	}

	private static String readClientColor(UUID playerId) {
		if (playerId == null)
			return null;
		File file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/minigames"), File.separator + "color.json");
		if (!file.exists())
			return null;
		try (java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.FileReader(file))) {
			StringBuilder jsonstringbuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonstringbuilder.append(line);
			}
			com.google.gson.JsonObject parsed = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
			if (parsed != null && parsed.has(playerId.toString()))
				return parsed.get(playerId.toString()).getAsString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
