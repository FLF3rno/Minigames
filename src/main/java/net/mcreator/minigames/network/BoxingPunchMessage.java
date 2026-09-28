package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.procedures.CheckRelicProcedure;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record BoxingPunchMessage(boolean isRight) implements CustomPacketPayload {
	public static final Type<BoxingPunchMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "boxing_punch"));
	public static final StreamCodec<RegistryFriendlyByteBuf, BoxingPunchMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, BoxingPunchMessage message) -> buffer.writeBoolean(message.isRight),
		(RegistryFriendlyByteBuf buffer) -> new BoxingPunchMessage(buffer.readBoolean())
	);

	@Override
	public Type<BoxingPunchMessage> type() {
		return TYPE;
	}

	public static void handleData(final BoxingPunchMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player player = context.player();
				if (player == null)
					return;
				ItemStack relic = CheckRelicProcedure.getRelicStack(player, new ItemStack(MinigamesModItems.BOXING_MASTERY.get()));
				if (!relic.isEmpty() && player.getMainHandItem().isEmpty()) {
					String anim = message.isRight() ? "minigames:right_punch" : "minigames:left_punch";
					if (player.level() instanceof ServerLevel serverLevel) {
						for (ServerPlayer other : serverLevel.players()) {
							if (other != player) {
								PacketDistributor.sendToPlayer(other, new PlayPlayerAnimationMessage(player.getId(), anim, true, true));
							}
						}
					}
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(BoxingPunchMessage.TYPE, BoxingPunchMessage.STREAM_CODEC, BoxingPunchMessage::handleData);
	}
}
