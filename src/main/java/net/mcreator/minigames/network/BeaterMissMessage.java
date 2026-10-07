package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.procedures.BeaterHitProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record BeaterMissMessage() implements CustomPacketPayload {
	public static final Type<BeaterMissMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "beater_miss"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BeaterMissMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, BeaterMissMessage message) -> {},
		(RegistryFriendlyByteBuf buffer) -> new BeaterMissMessage()
	);

	@Override
	public Type<BeaterMissMessage> type() {
		return TYPE;
	}

	public static void handleData(final BeaterMissMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player != null) {
					BeaterHitProcedure.processBeaterMiss(player);
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(BeaterMissMessage.TYPE, BeaterMissMessage.STREAM_CODEC, BeaterMissMessage::handleData);
	}
}
