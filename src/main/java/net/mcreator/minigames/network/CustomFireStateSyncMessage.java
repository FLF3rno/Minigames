package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.CustomFireHelper;
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
public record CustomFireStateSyncMessage(int entityId, int fireColorARGB) implements CustomPacketPayload {
	public static final Type<CustomFireStateSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "custom_fire_state_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CustomFireStateSyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, CustomFireStateSyncMessage message) -> {
			buffer.writeInt(message.entityId());
			buffer.writeInt(message.fireColorARGB());
		},
		(RegistryFriendlyByteBuf buffer) -> new CustomFireStateSyncMessage(buffer.readInt(), buffer.readInt())
	);

	@Override
	public Type<CustomFireStateSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final CustomFireStateSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				CustomFireHelper.handleClientSync(message.entityId(), message.fireColorARGB());
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, CustomFireStateSyncMessage::handleData);
	}
}
