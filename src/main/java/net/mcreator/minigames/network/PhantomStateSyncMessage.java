package net.mcreator.minigames.network;

import io.netty.buffer.ByteBuf;
import net.mcreator.minigames.MinigamesMod;
import net.minecraft.network.codec.ByteBufCodecs;
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
public record PhantomStateSyncMessage(int entityId, boolean isPhantom) implements CustomPacketPayload {
    public static final Type<PhantomStateSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "phantom_state_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PhantomStateSyncMessage> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buffer, PhantomStateSyncMessage message) -> {
                buffer.writeInt(message.entityId());
                buffer.writeBoolean(message.isPhantom());
            },
            (RegistryFriendlyByteBuf buffer) -> new PhantomStateSyncMessage(buffer.readInt(), buffer.readBoolean())
    );

    // Client-side tracker of phantom entity IDs
    private static final Set<Integer> CLIENT_PHANTOM_ENTITIES = ConcurrentHashMap.newKeySet();

    public static boolean isPhantomOnClient(int entityId) {
        return CLIENT_PHANTOM_ENTITIES.contains(entityId);
    }

    @Override
    public Type<PhantomStateSyncMessage> type() {
        return TYPE;
    }

    public static void handleData(final PhantomStateSyncMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            context.enqueueWork(() -> {
                if (message.isPhantom) {
                    CLIENT_PHANTOM_ENTITIES.add(message.entityId);
                } else {
                    CLIENT_PHANTOM_ENTITIES.remove(message.entityId);
                }
            });
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, PhantomStateSyncMessage::handleData);
    }
}
