package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record AchievementLobbyRequestMessage() implements CustomPacketPayload {
	public static final Type<AchievementLobbyRequestMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "achievement_lobby_request"));

	public static final StreamCodec<RegistryFriendlyByteBuf, AchievementLobbyRequestMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, AchievementLobbyRequestMessage message) -> {},
		(RegistryFriendlyByteBuf buffer) -> new AchievementLobbyRequestMessage()
	);

	@Override
	public Type<AchievementLobbyRequestMessage> type() {
		return TYPE;
	}

	public static void handleData(final AchievementLobbyRequestMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND && context.player() instanceof ServerPlayer sender) {
			context.enqueueWork(() -> {
				if (sender.level().getServer() != null) {
					for (ServerPlayer player : sender.level().getServer().getPlayerList().getPlayers()) {
						MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
						PacketDistributor.sendToPlayer(sender, new AchievementLobbySyncMessage(player.getUUID(), vars.AchievementLobbyState, (int) vars.team));
					}
				}
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, AchievementLobbyRequestMessage::handleData);
	}
}
