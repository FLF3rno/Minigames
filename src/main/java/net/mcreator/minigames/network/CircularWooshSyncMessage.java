package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.ClientWooshManager;
import net.minecraft.network.FriendlyByteBuf;
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
public record CircularWooshSyncMessage(
	double x,
	double y,
	double z,
	int entityId,
	float size,
	float speed,
	int ticks,
	int color
) implements CustomPacketPayload {
	public static final Type<CircularWooshSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "circular_woosh_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CircularWooshSyncMessage> STREAM_CODEC = StreamCodec.of(
		CircularWooshSyncMessage::write,
		CircularWooshSyncMessage::read
	);

	public static void write(FriendlyByteBuf buffer, CircularWooshSyncMessage message) {
		buffer.writeDouble(message.x());
		buffer.writeDouble(message.y());
		buffer.writeDouble(message.z());
		buffer.writeInt(message.entityId());
		buffer.writeFloat(message.size());
		buffer.writeFloat(message.speed());
		buffer.writeInt(message.ticks());
		buffer.writeInt(message.color());
	}

	public static CircularWooshSyncMessage read(FriendlyByteBuf buffer) {
		return new CircularWooshSyncMessage(
			buffer.readDouble(),
			buffer.readDouble(),
			buffer.readDouble(),
			buffer.readInt(),
			buffer.readFloat(),
			buffer.readFloat(),
			buffer.readInt(),
			buffer.readInt()
		);
	}

	@Override
	public Type<CircularWooshSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final CircularWooshSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				ClientWooshManager.addWoosh(
					message.x(),
					message.y(),
					message.z(),
					message.entityId(),
					message.size(),
					message.speed(),
					message.ticks(),
					message.color()
				);
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, CircularWooshSyncMessage::handleData);
	}
}
