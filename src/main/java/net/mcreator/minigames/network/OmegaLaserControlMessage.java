package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record OmegaLaserControlMessage(int laserEntityId, int screenIndex, int action, float deltaPitch, float deltaYaw) implements CustomPacketPayload {
	public static final Type<OmegaLaserControlMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "omega_laser_control"));

	// Action constants:
	// 0 = Move / Steer (deltaPitch, deltaYaw)
	// 1 = Fire Button
	// 2 = Exit Screen GUI

	public static final StreamCodec<RegistryFriendlyByteBuf, OmegaLaserControlMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, OmegaLaserControlMessage message) -> {
				buffer.writeInt(message.laserEntityId());
				buffer.writeInt(message.screenIndex());
				buffer.writeInt(message.action());
				buffer.writeFloat(message.deltaPitch());
				buffer.writeFloat(message.deltaYaw());
			},
			(RegistryFriendlyByteBuf buffer) -> new OmegaLaserControlMessage(
					buffer.readInt(),
					buffer.readInt(),
					buffer.readInt(),
					buffer.readFloat(),
					buffer.readFloat()
			)
	);

	@Override
	public Type<OmegaLaserControlMessage> type() {
		return TYPE;
	}

	public static void handleData(final OmegaLaserControlMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer player) {
					Entity target = player.level().getEntity(message.laserEntityId());
					if (target instanceof FlavioOmegaLaserEntity laser && laser.isAlive()) {
						if (message.action() == 0) {
							laser.handleSteerInput(player, message.screenIndex(), message.deltaPitch(), message.deltaYaw());
						} else if (message.action() == 1) {
							laser.handleFireInput(player, message.screenIndex());
						} else if (message.action() == 2) {
							laser.handleScreenExit(player, message.screenIndex());
						}
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
		MinigamesMod.addNetworkMessage(OmegaLaserControlMessage.TYPE, OmegaLaserControlMessage.STREAM_CODEC, OmegaLaserControlMessage::handleData);
	}
}
