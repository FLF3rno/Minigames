package net.mcreator.minigames.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.procedures.ApplyClassProcedure;

@EventBusSubscriber
public record SelectClassMessage(String selectedClass) implements CustomPacketPayload {
	public static final Type<SelectClassMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "select_class"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SelectClassMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, SelectClassMessage message) -> buffer.writeUtf(message.selectedClass),
			(RegistryFriendlyByteBuf buffer) -> new SelectClassMessage(buffer.readUtf())
	);

	@Override
	public Type<SelectClassMessage> type() {
		return TYPE;
	}

	public static void handleData(final SelectClassMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					ApplyClassProcedure.execute(serverPlayer, message.selectedClass);
					serverPlayer.closeContainer();
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(SelectClassMessage.TYPE, SelectClassMessage.STREAM_CODEC, SelectClassMessage::handleData);
	}
}
