package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public record GoldenStateSyncMessage(int entityId, boolean isGolden) implements CustomPacketPayload {
	public static final Type<GoldenStateSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_state_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, GoldenStateSyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, GoldenStateSyncMessage message) -> {
			buffer.writeInt(message.entityId());
			buffer.writeBoolean(message.isGolden());
		},
		(RegistryFriendlyByteBuf buffer) -> new GoldenStateSyncMessage(buffer.readInt(), buffer.readBoolean())
	);

	private static final Set<Integer> CLIENT_GOLDEN_ENTITIES = ConcurrentHashMap.newKeySet();

	public static boolean isGoldenOnClient(int entityId) {
		return CLIENT_GOLDEN_ENTITIES.contains(entityId);
	}

	@Override
	public Type<GoldenStateSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final GoldenStateSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				if (message.isGolden) {
					CLIENT_GOLDEN_ENTITIES.add(message.entityId);
				} else {
					CLIENT_GOLDEN_ENTITIES.remove(message.entityId);
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, GoldenStateSyncMessage::handleData);
	}
}
