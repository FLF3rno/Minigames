package net.mcreator.minigames.client.gui.options;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.neoforged.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MinigamesSettingsConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static JsonObject cache = null;

	private static File getConfigFile() {
		return new File(FMLPaths.GAMEDIR.get().toFile(), "config/minigames_settings.json");
	}

	private static synchronized JsonObject getJson() {
		if (cache != null) {
			return cache;
		}
		File file = getConfigFile();
		cache = new JsonObject();
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				JsonObject parsed = GSON.fromJson(sb.toString(), JsonObject.class);
				if (parsed != null) {
					cache = parsed;
				}
			} catch (Throwable ignored) {
				cache = new JsonObject();
			}
		}
		return cache;
	}

	public static synchronized boolean getBoolean(String key, boolean defaultValue) {
		JsonObject json = getJson();
		if (json.has(key)) {
			try {
				com.google.gson.JsonElement elem = json.get(key);
				if (elem != null && elem.isJsonPrimitive()) {
					com.google.gson.JsonPrimitive prim = elem.getAsJsonPrimitive();
					if (prim.isBoolean()) {
						return prim.getAsBoolean();
					}
					if (prim.isString()) {
						String s = prim.getAsString();
						if ("true".equalsIgnoreCase(s)) {
							return true;
						}
						if ("false".equalsIgnoreCase(s)) {
							return false;
						}
					}
					if (prim.isNumber()) {
						return prim.getAsNumber().intValue() != 0;
					}
				}
			} catch (Throwable ignored) {
			}
		}
		return defaultValue;
	}

	public static synchronized void setBoolean(String key, boolean value) {
		JsonObject json = getJson();
		json.addProperty(key, value);
		save();
	}

	public static synchronized double getDouble(String key, double defaultValue) {
		JsonObject json = getJson();
		if (json.has(key)) {
			try {
				com.google.gson.JsonElement elem = json.get(key);
				if (elem != null && elem.isJsonPrimitive()) {
					com.google.gson.JsonPrimitive prim = elem.getAsJsonPrimitive();
					if (prim.isNumber()) {
						return prim.getAsDouble();
					}
					if (prim.isString()) {
						return Double.parseDouble(prim.getAsString());
					}
					if (prim.isBoolean()) {
						return prim.getAsBoolean() ? 1.0 : 0.0;
					}
				}
			} catch (Throwable ignored) {
			}
		}
		return defaultValue;
	}

	public static synchronized void setDouble(String key, double value) {
		JsonObject json = getJson();
		json.addProperty(key, value);
		save();
	}

	public static synchronized String getString(String key, String defaultValue) {
		JsonObject json = getJson();
		if (json.has(key)) {
			try {
				com.google.gson.JsonElement elem = json.get(key);
				if (elem != null && elem.isJsonPrimitive()) {
					return elem.getAsString();
				}
			} catch (Throwable ignored) {
			}
		}
		return defaultValue;
	}

	public static synchronized void setString(String key, String value) {
		JsonObject json = getJson();
		json.addProperty(key, value);
		save();
	}

	public static synchronized void save() {
		File file = getConfigFile();
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			try (FileWriter writer = new FileWriter(file)) {
				GSON.toJson(getJson(), writer);
			}
		} catch (Exception ignored) {
		}
	}
}
