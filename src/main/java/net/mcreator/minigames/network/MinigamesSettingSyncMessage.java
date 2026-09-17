package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.gui.options.MinigamesSettingsHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record MinigamesSettingSyncMessage(String key, double doubleVal, boolean boolVal, boolean isBoolean) implements CustomPacketPayload {
	public static final Type<MinigamesSettingSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "setting_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MinigamesSettingSyncMessage> STREAM_CODEC = StreamCodec.of(MinigamesSettingSyncMessage::write, MinigamesSettingSyncMessage::read);

	public static void write(FriendlyByteBuf buffer, MinigamesSettingSyncMessage message) {
		buffer.writeUtf(message.key == null ? "" : message.key);
		buffer.writeDouble(message.doubleVal);
		buffer.writeBoolean(message.boolVal);
		buffer.writeBoolean(message.isBoolean);
	}

	public static MinigamesSettingSyncMessage read(FriendlyByteBuf buffer) {
		return new MinigamesSettingSyncMessage(buffer.readUtf(), buffer.readDouble(), buffer.readBoolean(), buffer.readBoolean());
	}

	@Override
	public Type<MinigamesSettingSyncMessage> type() {
		return TYPE;
	}

	public static void handle(final MinigamesSettingSyncMessage message, final IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.flow() == PacketFlow.SERVERBOUND && context.player() instanceof ServerPlayer serverPlayer) {
				MinigamesSettingsHelper.applySettingDirectly(message.key(), message.doubleVal(), message.boolVal(), message.isBoolean(), serverPlayer.level(), serverPlayer);
			}
		}).exceptionally(e -> {
			context.connection().disconnect(net.minecraft.network.chat.Component.literal(e.getMessage()));
			return null;
		});
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(MinigamesSettingSyncMessage.TYPE, MinigamesSettingSyncMessage.STREAM_CODEC, MinigamesSettingSyncMessage::handle);
	}
}
