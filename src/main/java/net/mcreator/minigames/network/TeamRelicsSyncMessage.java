package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.procedures.CheckAllPlayersRelicProcedure;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@EventBusSubscriber
public record TeamRelicsSyncMessage(List<ItemStack> relics) implements CustomPacketPayload {
	public static final Type<TeamRelicsSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "team_relics_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, TeamRelicsSyncMessage> STREAM_CODEC = StreamCodec.composite(
			ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
			TeamRelicsSyncMessage::relics,
			TeamRelicsSyncMessage::new
	);

	@Override
	public Type<TeamRelicsSyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final TeamRelicsSyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> CheckAllPlayersRelicProcedure.setClientRelics(message.relics()));
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, TeamRelicsSyncMessage::handleData);
	}
}
