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

					net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) serverPlayer.level();
					// Play ding sound to all players in the dimension whenever a player selects a class
					for (ServerPlayer p : serverLevel.players()) {
						serverLevel.playSound(null, p.getX(), p.getY(), p.getZ(), net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
					}

					java.util.List<ServerPlayer> playersInSelection = new java.util.ArrayList<>();
					for (ServerPlayer player : serverLevel.players()) {
						if (player.containerMenu instanceof net.mcreator.minigames.world.inventory.ClassSelectionRoguelikeMenu) {
							playersInSelection.add(player);
						}
					}
					if (playersInSelection.isEmpty()) {
						playersInSelection.addAll(serverLevel.players());
					}

					boolean allReady = true;
					for (ServerPlayer player : playersInSelection) {
						String chosen = player.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
						if (chosen == null || chosen.trim().isEmpty() || chosen.equalsIgnoreCase("none")) {
							allReady = false;
							break;
						}
					}

					if (allReady) {
						// Hold for 2s (40 ticks), then play beacon activation sound and let screen fade out over 2s (40 ticks) before closing
						MinigamesMod.queueServerWork(40, () -> {
							for (ServerPlayer p : serverLevel.players()) {
								serverLevel.playSound(null, p.getX(), p.getY(), p.getZ(), net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
							}
							MinigamesMod.queueServerWork(40, () -> {
								for (ServerPlayer p : serverLevel.players()) {
									if (p.containerMenu instanceof net.mcreator.minigames.world.inventory.ClassSelectionRoguelikeMenu) {
										p.closeContainer();
									}
								}
							});
						});
					}
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
