package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.AchievementLobbyClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public record AchievementLobbySyncMessage(UUID uuid, String state, int team) implements CustomPacketPayload {
	public static final Type<AchievementLobbySyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "achievement_lobby_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, AchievementLobbySyncMessage> STREAM_CODEC = StreamCodec.of(
		(RegistryFriendlyByteBuf buffer, AchievementLobbySyncMessage message) -> {
			buffer.writeUUID(message.uuid());
			buffer.writeUtf(message.state() != null ? message.state() : "");
			buffer.writeInt(message.team());
		},
		(RegistryFriendlyByteBuf buffer) -> new AchievementLobbySyncMessage(buffer.readUUID(), buffer.readUtf(), buffer.readInt())
	);

	@Override
	public Type<AchievementLobbySyncMessage> type() {
		return TYPE;
	}

	public static void handleData(final AchievementLobbySyncMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				AchievementLobbyClient.update(message.uuid(), message.state(), message.team());
			});
		}
	}

	public static void broadcast(UUID uuid, String state, int team) {
		PacketDistributor.sendToAllPlayers(new AchievementLobbySyncMessage(uuid, state, team));
	}

	public static void broadcastReset() {
		PacketDistributor.sendToAllPlayers(new AchievementLobbySyncMessage(new UUID(0L, 0L), "RESET_ALL", 0));
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TYPE, STREAM_CODEC, AchievementLobbySyncMessage::handleData);
	}

	@EventBusSubscriber
	public static class ServerWatcher {
		private static final Map<UUID, String> LAST_STATES = new HashMap<>();
		private static final Map<UUID, Integer> LAST_TEAMS = new HashMap<>();
		private static int tickCounter = 0;

		@SubscribeEvent
		public static void onServerTick(ServerTickEvent.Post event) {
			if (++tickCounter % 5 != 0)
				return;
			net.minecraft.server.MinecraftServer server = event.getServer();
			if (server == null)
				return;
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
				String currentState = vars.AchievementLobbyState != null ? vars.AchievementLobbyState : "";
				int currentTeam = (int) vars.team;
				String lastState = LAST_STATES.get(player.getUUID());
				Integer lastTeam = LAST_TEAMS.get(player.getUUID());
				if (!currentState.equals(lastState) || lastTeam == null || lastTeam != currentTeam) {
					LAST_STATES.put(player.getUUID(), currentState);
					LAST_TEAMS.put(player.getUUID(), currentTeam);
					broadcast(player.getUUID(), currentState, currentTeam);
				}
			}
		}
	}
}
