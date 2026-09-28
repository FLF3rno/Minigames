package net.mcreator.minigames.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.AccountSwapManager;

import java.util.UUID;

@EventBusSubscriber
public record AccountSwapSyncMessage(
	UUID playerUUID,
	String swappedName,
	UUID skinOwnerUUID,
	String skinOwnerName,
	boolean isReset
) implements CustomPacketPayload {
	public static final Type<AccountSwapSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "account_swap_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, AccountSwapSyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, AccountSwapSyncMessage message) -> {
			buffer.writeUUID(message.playerUUID());
			buffer.writeUtf(message.swappedName() == null ? "" : message.swappedName());
			buffer.writeBoolean(message.skinOwnerUUID() != null);
			if (message.skinOwnerUUID() != null) {
				buffer.writeUUID(message.skinOwnerUUID());
			}
			buffer.writeUtf(message.skinOwnerName() == null ? "" : message.skinOwnerName());
			buffer.writeBoolean(message.isReset());
		},
		(RegistryFriendlyByteBuf buffer) -> {
			UUID playerUUID = buffer.readUUID();
			String swappedName = buffer.readUtf();
			UUID skinOwnerUUID = buffer.readBoolean() ? buffer.readUUID() : null;
			String skinOwnerName = buffer.readUtf();
			boolean isReset = buffer.readBoolean();
			return new AccountSwapSyncMessage(playerUUID, swappedName, skinOwnerUUID, skinOwnerName, isReset);
		}
	);

	@Override
	public Type<AccountSwapSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final AccountSwapSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				AccountSwapManager.handleClientSync(
					message.playerUUID(),
					message.swappedName(),
					message.skinOwnerUUID(),
					message.skinOwnerName(),
					message.isReset()
				);
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, AccountSwapSyncMessage::handleData);
	}
}
