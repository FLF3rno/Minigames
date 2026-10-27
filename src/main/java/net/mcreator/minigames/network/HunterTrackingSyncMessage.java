package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.HunterTrackerClient;
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
public record HunterTrackingSyncMessage(String dimensionId, double x, double z, boolean isCurrentDimension) implements CustomPacketPayload {
	public static final Type<HunterTrackingSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hunter_tracking_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, HunterTrackingSyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, HunterTrackingSyncMessage message) -> {
			buffer.writeUtf(message.dimensionId());
			buffer.writeDouble(message.x());
			buffer.writeDouble(message.z());
			buffer.writeBoolean(message.isCurrentDimension());
		},
		(RegistryFriendlyByteBuf buffer) -> new HunterTrackingSyncMessage(buffer.readUtf(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean())
	);

	@Override
	public Type<HunterTrackingSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final HunterTrackingSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				HunterTrackerClient.update(message.dimensionId(), message.x(), message.z(), message.isCurrentDimension());
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, HunterTrackingSyncMessage::handleData);
	}
}
