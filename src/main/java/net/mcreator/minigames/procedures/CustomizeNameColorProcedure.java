package net.mcreator.minigames.procedures;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.NameColorPreferenceMessage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CustomizeNameColorProcedure {
	public static void executeDirectColor(LevelAccessor world, Entity entity, String hexColor) {
		if (entity == null || hexColor == null)
			return;
		String normalized = hexColor.trim();
		if (!normalized.matches("^#?[0-9a-fA-F]{6}$"))
			return;
		if (!normalized.startsWith("#"))
			normalized = "#" + normalized;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.color = normalized;
			_vars.showCustomNameColor = true;
			_vars.markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).applyCustomNameColor = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (entity instanceof ServerPlayer _player)
			PacketDistributor.sendToPlayer(_player, new NameColorPreferenceMessage(normalized));
	}

	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		com.google.gson.JsonObject mainObj = new com.google.gson.JsonObject();
		String playerKey = (entity instanceof Player _player) ? _player.getUUID().toString() : entity.getStringUUID();
		if (!((StringArgumentType.getString(arguments, "color")).equals("dark_gray") || (StringArgumentType.getString(arguments, "color")).equals("gray") || (StringArgumentType.getString(arguments, "color")).equals("black"))) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.color = StringArgumentType.getString(arguments, "color");
				_vars.showCustomNameColor = true;
				_vars.markSyncDirty();
			}
			MinigamesModVariables.MapVariables.get(world).applyCustomNameColor = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (entity instanceof ServerPlayer _player)
				PacketDistributor.sendToPlayer(_player, new NameColorPreferenceMessage(StringArgumentType.getString(arguments, "color")));
			file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/minigames"), File.separator + "color.json");
			if (file.exists()) {
				try (java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new java.io.FileReader(file))) {
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					com.google.gson.JsonObject parsed = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					if (parsed != null) {
						mainObj = parsed;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			mainObj.addProperty(playerKey, StringArgumentType.getString(arguments, "color"));
			{
				com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(mainGSONBuilderVariable.toJson(mainObj));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
