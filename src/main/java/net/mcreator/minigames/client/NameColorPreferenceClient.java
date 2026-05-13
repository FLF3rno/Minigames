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
	private static UUID loadedForPlayer = null;
	private static boolean sentThisSession = false;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null) {
			loadedForPlayer = null;
			sentThisSession = false;
			return;
		}
		UUID currentId = player.getUUID();
		if (loadedForPlayer == null || !loadedForPlayer.equals(currentId)) {
			String persistedColor = readClientColor(currentId);
			if (persistedColor != null && persistedColor.matches("^#?[0-9a-fA-F]{6}$")) {
				persistedColor = persistedColor.startsWith("#") ? persistedColor : "#" + persistedColor;
				player.getData(net.mcreator.minigames.network.MinigamesModVariables.PLAYER_VARIABLES).color = persistedColor;
			}
			loadedForPlayer = currentId;
		}
		if (!sentThisSession) {
			sendCurrentVariableColorNow(player);
			sentThisSession = true;
		}
	}

	public static void sendCurrentVariableColorNow(Player player) {
		if (player == null)
			return;
		String color = player.getData(net.mcreator.minigames.network.MinigamesModVariables.PLAYER_VARIABLES).color;
		if (color == null || !color.matches("^#?[0-9a-fA-F]{6}$"))
			return;
		color = color.startsWith("#") ? color : "#" + color;
		ClientPacketDistributor.sendToServer(new NameColorPreferenceMessage(color));
		writeClientColorNow(player.getUUID(), color);
	}

	private static String readClientColor(UUID playerId) {
		if (playerId == null)
			return null;
		File file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/minigames"), File.separator + "color.json");
		if (!file.exists()) {
			writeClientColorNow(playerId, "#FFFFFF");
			return "#FFFFFF";
		}
		try (java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.FileReader(file))) {
			StringBuilder jsonstringbuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				jsonstringbuilder.append(line);
			}
			com.google.gson.JsonObject parsed = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
			if (parsed != null && parsed.has(playerId.toString())) {
				String value = parsed.get(playerId.toString()).getAsString();
				if (value != null && value.matches("^#?[0-9a-fA-F]{6}$")) {
					return value.startsWith("#") ? value : "#" + value;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		writeClientColorNow(playerId, "#FFFFFF");
		return "#FFFFFF";
	}

	public static void writeClientColorNow(UUID playerId, String color) {
		if (playerId == null || color == null || color.isEmpty())
			return;
		File file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/minigames"), File.separator + "color.json");
		com.google.gson.JsonObject mainObj = new com.google.gson.JsonObject();
		if (file.exists()) {
			try (java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.FileReader(file))) {
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				com.google.gson.JsonObject parsed = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				if (parsed != null)
					mainObj = parsed;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		mainObj.addProperty(playerId.toString(), color);
		try (java.io.FileWriter fileWriter = new java.io.FileWriter(file)) {
			fileWriter.write(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(mainObj));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
