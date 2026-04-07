package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

@EventBusSubscriber
public class ChangeNameColorProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		File file = new File("");
		com.google.gson.JsonObject mainObj = new com.google.gson.JsonObject();
		String playerKey = (entity instanceof net.minecraft.world.entity.player.Player _player) ? _player.getUUID().toString() : entity.getStringUUID();
		file = new File((FMLPaths.GAMEDIR.get().toString() + "/config/minigames"), File.separator + "color.json");
		if (file.exists()) {
			{
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
					StringBuilder jsonstringbuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						jsonstringbuilder.append(line);
					}
					bufferedReader.close();
					com.google.gson.JsonObject parsed = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
					if (parsed != null) {
						mainObj = parsed;
					}
					String legacyKey = entity.getDisplayName().getString();
					String legacyNameKey = entity.getName().getString();
					String resolvedKey = null;
					if (mainObj.has(playerKey) && mainObj.get(playerKey).isJsonPrimitive()) {
						resolvedKey = playerKey;
					} else if (mainObj.has(legacyKey) && mainObj.get(legacyKey).isJsonPrimitive()) {
						resolvedKey = legacyKey;
					} else if (mainObj.has(legacyNameKey) && mainObj.get(legacyNameKey).isJsonPrimitive()) {
						resolvedKey = legacyNameKey;
					}
					if (!(mainObj.size() == 0) && resolvedKey != null) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
										_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("customize name " + mainObj.get(resolvedKey).getAsString()));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		NameColorApplyProcedure.execute(world, entity);
	}
}
