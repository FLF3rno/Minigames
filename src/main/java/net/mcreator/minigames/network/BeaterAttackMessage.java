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
public record BeaterAttackMessage(int targetId, boolean onBeat, String rating) implements CustomPacketPayload {
	public static final Type<BeaterAttackMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "beater_attack"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BeaterAttackMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, BeaterAttackMessage message) -> {
			buffer.writeInt(message.targetId);
			buffer.writeBoolean(message.onBeat);
			buffer.writeUtf(message.rating);
		},
		(RegistryFriendlyByteBuf buffer) -> new BeaterAttackMessage(buffer.readInt(), buffer.readBoolean(), buffer.readUtf())
	);

	@Override
	public Type<BeaterAttackMessage> type() {
		return TYPE;
	}

	public static void handleData(final BeaterAttackMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player == null)
					return;
				BeaterHitProcedure.processBeaterHit(player, message.targetId(), message.onBeat(), message.rating());
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(BeaterAttackMessage.TYPE, BeaterAttackMessage.STREAM_CODEC, BeaterAttackMessage::handleData);
	}
}
