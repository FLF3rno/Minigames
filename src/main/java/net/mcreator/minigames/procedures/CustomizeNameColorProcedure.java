package net.mcreator.minigames.procedures;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.NameColorPreferenceMessage;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CustomizeNameColorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		com.google.gson.JsonObject mainObj = new com.google.gson.JsonObject();
		String teamName = (entity instanceof Player _player) ? _player.getGameProfile().getName() : entity.getStringUUID();
		String playerKey = (entity instanceof Player _player) ? _player.getUUID().toString() : entity.getStringUUID();
		if (!((StringArgumentType.getString(arguments, "color")).equals("dark_gray") || (StringArgumentType.getString(arguments, "color")).equals("gray") || (StringArgumentType.getString(arguments, "color")).equals("black"))) {
			if (world instanceof Level _level)
				_level.getScoreboard().addPlayerTeam(teamName);
			{
				Entity _entityTeam = entity;
				PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam(teamName);
				if (_pt != null) {
					if (_entityTeam instanceof Player _player)
						_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().getName(), _pt);
					else
						_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
				}
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						(("/team modify " + teamName) + "" + (" color " + StringArgumentType.getString(arguments, "color"))));
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.color = StringArgumentType.getString(arguments, "color");
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
