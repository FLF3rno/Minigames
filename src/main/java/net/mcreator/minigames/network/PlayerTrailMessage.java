package net.mcreator.minigames.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.ClientPlayerTrailManager;

@EventBusSubscriber
public record PlayerTrailMessage(int entityId, boolean fadeOut, double cloneDuration, double opacity, double ticks, String color) implements CustomPacketPayload {
	public static final Type<PlayerTrailMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "player_trail"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayerTrailMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, PlayerTrailMessage message) -> {
				buffer.writeInt(message.entityId);
				buffer.writeBoolean(message.fadeOut);
				buffer.writeDouble(message.cloneDuration);
				buffer.writeDouble(message.opacity);
				buffer.writeDouble(message.ticks);
				buffer.writeUtf(message.color != null ? message.color : "");
			},
			(RegistryFriendlyByteBuf buffer) -> new PlayerTrailMessage(
					buffer.readInt(),
					buffer.readBoolean(),
					buffer.readDouble(),
					buffer.readDouble(),
					buffer.readDouble(),
					buffer.readUtf()
			)
	);

	@Override
	public Type<PlayerTrailMessage> type() {
		return TYPE;
	}

	public static void handleData(final PlayerTrailMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				ClientPlayerTrailManager.startTrail(
						message.entityId,
						message.fadeOut,
						message.cloneDuration,
						message.opacity,
						message.ticks,
						message.color
				);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, PlayerTrailMessage::handleData);
	}
}
