package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.procedures.ForceStopMinigameProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record StopMinigameMessage(String extradata) implements CustomPacketPayload {
	public static final Type<StopMinigameMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "stop_minigame"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StopMinigameMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, StopMinigameMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new StopMinigameMessage(buffer.readUtf()));

	@Override
	public Type<StopMinigameMessage> type() {
		return TYPE;
	}

	public static void handleData(final StopMinigameMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;
				if (!world.hasChunkAt(entity.blockPosition()))
					return;

				ForceStopMinigameProcedure.execute(world, x, y, z, entity);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(StopMinigameMessage.TYPE, StopMinigameMessage.STREAM_CODEC, StopMinigameMessage::handleData);
	}
}