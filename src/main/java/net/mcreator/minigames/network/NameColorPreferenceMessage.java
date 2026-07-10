package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.procedures.NameColorApplyProcedure;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

@EventBusSubscriber
public record NameColorPreferenceMessage(String color) implements CustomPacketPayload {
	public static final Type<NameColorPreferenceMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "name_color_preference"));
	public static final StreamCodec<RegistryFriendlyByteBuf, NameColorPreferenceMessage> STREAM_CODEC = StreamCodec.of(NameColorPreferenceMessage::write, NameColorPreferenceMessage::read);

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
			if (!color.matches("^#?[0-9a-fA-F]{6}$"))
				return;
			color = color.startsWith("#") ? color : "#" + color;
			MinigamesModVariables.PlayerVariables vars = serverPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES);
			vars.color = color;
			vars.showCustomNameColor = true;
			vars.markSyncDirty();
			NameColorApplyProcedure.applyColor(serverPlayer.level(), serverPlayer);
			applyCustomNameColorNow(serverPlayer, color);
			serverPlayer.refreshTabListName();
			if (serverPlayer.level().getServer() != null) {
				serverPlayer.level().getServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, serverPlayer));
			}
			MinigamesModVariables.MapVariables.get(serverPlayer.level()).applyCustomNameColor = true;
			MinigamesModVariables.MapVariables.get(serverPlayer.level()).markSyncDirty();
			PacketDistributor.sendToPlayer(serverPlayer, new NameColorPreferenceMessage(color));
		}).exceptionally(e -> {
			context.connection().disconnect(Component.literal(e.getMessage()));
			return null;
		});
	}

	private static void applyCustomNameColorNow(ServerPlayer player, String hexColor) {
		TextColor parsedColor = TextColor.parseColor(hexColor).result().orElse(null);
		if (parsedColor == null)
			return;
		player.setCustomName(Component.literal(player.getName().getString()).setStyle(Style.EMPTY.withColor(parsedColor)));
		player.setCustomNameVisible(true);
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



