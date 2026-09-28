package net.mcreator.minigames.network;

import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.mixin.LivingEntityAccessor;

@EventBusSubscriber
public record SyncAttackStrengthMessage(int ticker) implements CustomPacketPayload {
	public static final Type<SyncAttackStrengthMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "sync_attack_strength"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SyncAttackStrengthMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, SyncAttackStrengthMessage message) -> buffer.writeInt(message.ticker()),
		(RegistryFriendlyByteBuf buffer) -> new SyncAttackStrengthMessage(buffer.readInt())
	);

	@Override
	public Type<SyncAttackStrengthMessage> type() {
		return TYPE;
	}

	public static void handleData(final SyncAttackStrengthMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof LivingEntityAccessor accessor) {
					accessor.setAttackStrengthTicker(message.ticker());
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, SyncAttackStrengthMessage::handleData);
	}
}
