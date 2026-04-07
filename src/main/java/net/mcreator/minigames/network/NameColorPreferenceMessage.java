package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;

import net.neoforged.fml.loading.FMLPaths;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.Set;
import java.util.UUID;
import java.io.File;
import java.io.FileWriter;

@EventBusSubscriber
public record NameColorPreferenceMessage(String color) implements CustomPacketPayload {
	public static final Type<NameColorPreferenceMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "name_color_preference"));
	public static final StreamCodec<RegistryFriendlyByteBuf, NameColorPreferenceMessage> STREAM_CODEC = StreamCodec.of(NameColorPreferenceMessage::write, NameColorPreferenceMessage::read);
	private static final Set<String> ALLOWED_COLORS = Set.of("white", "aqua", "dark_aqua", "blue", "dark_blue", "green", "dark_green", "light_purple", "dark_purple", "red", "dark_red", "yellow", "gold");

	public static void write(FriendlyByteBuf buffer, NameColorPreferenceMessage message) {
		buffer.writeUtf(message.color == null ? "" : message.color);
	}

	public static NameColorPreferenceMessage read(FriendlyByteBuf buffer) {
		return new NameColorPreferenceMessage(buffer.readUtf());
	}

	@Override
	public Type<NameColorPreferenceMessage> type() {
		return TYPE;
	}

	public static void handle(final NameColorPreferenceMessage message, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.flow() == PacketFlow.CLIENTBOUND) {
				Player player = context.player();
				if (player != null)
					writeClientColor(player.getUUID(), message.color);
				return;
			}
			Entity sender = context.player();
			if (!(sender instanceof ServerPlayer serverPlayer))
				return;
			String color = message.color == null ? "" : message.color;
			if (!ALLOWED_COLORS.contains(color))
				return;
			ServerLevel level = serverPlayer.level();
			String teamName = serverPlayer.getGameProfile().getName();
			if (level.getScoreboard().getPlayerTeam(teamName) == null)
				level.getScoreboard().addPlayerTeam(teamName);
			PlayerTeam team = level.getScoreboard().getPlayerTeam(teamName);
			if (team != null) {
				level.getScoreboard().addPlayerToTeam(serverPlayer.getGameProfile().getName(), team);
				if (team.getColor() == null || !team.getColor().getName().equals(color)) {
					level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()), Vec2.ZERO, level, 4, "", Component.literal(""), level.getServer(), null)
									.withSuppressedOutput(),
							"/team modify " + teamName + " color " + color);
				}
			}
			MinigamesModVariables.PlayerVariables vars = serverPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES);
			vars.color = color;
			vars.markSyncDirty();
			PacketDistributor.sendToPlayer(serverPlayer, new NameColorPreferenceMessage(color));
		}).exceptionally(e -> {
			context.connection().disconnect(Component.literal(e.getMessage()));
			return null;
		});
	}

	private static void writeClientColor(UUID playerId, String color) {
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
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		mainObj.addProperty(playerId.toString(), color);
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(mainObj));
			fileWriter.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(NameColorPreferenceMessage.TYPE, NameColorPreferenceMessage.STREAM_CODEC, NameColorPreferenceMessage::handle);
	}
}
