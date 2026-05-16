package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record VoteNoMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<VoteNoMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "key_vote_no"));
	public static final StreamCodec<RegistryFriendlyByteBuf, VoteNoMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, VoteNoMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new VoteNoMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<VoteNoMessage> type() {
		return TYPE;
	}

	public static void handleData(final VoteNoMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(VoteNoMessage.TYPE, VoteNoMessage.STREAM_CODEC, VoteNoMessage::handleData);
	}
}