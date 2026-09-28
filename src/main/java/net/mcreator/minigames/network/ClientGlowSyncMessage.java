package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.ClientGlowManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record ClientGlowSyncMessage(int entityId, int color, int ticks) implements CustomPacketPayload {
	public static final Type<ClientGlowSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "client_glow_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientGlowSyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, ClientGlowSyncMessage message) -> {
			buffer.writeInt(message.entityId());
			buffer.writeInt(message.color());
			buffer.writeInt(message.ticks());
		},
		(RegistryFriendlyByteBuf buffer) -> new ClientGlowSyncMessage(buffer.readInt(), buffer.readInt(), buffer.readInt())
	);

	@Override
	public Type<ClientGlowSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final ClientGlowSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				if (message.ticks() <= 0) {
					ClientGlowManager.removeGlow(message.entityId());
				} else {
					ClientGlowManager.addGlow(message.entityId(), message.color(), message.ticks());
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, ClientGlowSyncMessage::handleData);
	}
}
