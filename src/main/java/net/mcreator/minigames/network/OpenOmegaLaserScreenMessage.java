package net.mcreator.minigames.network;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.gui.OmegaLaserScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public record OpenOmegaLaserScreenMessage(int laserEntityId, int screenIndex, int totalPlayers) implements CustomPacketPayload {
	public static final Type<OpenOmegaLaserScreenMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "open_omega_laser_screen"));

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenOmegaLaserScreenMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, OpenOmegaLaserScreenMessage message) -> {
				buffer.writeInt(message.laserEntityId());
				buffer.writeInt(message.screenIndex());
				buffer.writeInt(message.totalPlayers());
			},
			(RegistryFriendlyByteBuf buffer) -> new OpenOmegaLaserScreenMessage(
					buffer.readInt(),
					buffer.readInt(),
					buffer.readInt()
			)
	);

	@Override
	public Type<OpenOmegaLaserScreenMessage> type() {
		return TYPE;
	}

	public static void handleData(final OpenOmegaLaserScreenMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				Minecraft.getInstance().setScreen(new OmegaLaserScreen(message.laserEntityId(), message.screenIndex(), message.totalPlayers()));
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(OpenOmegaLaserScreenMessage.TYPE, OpenOmegaLaserScreenMessage.STREAM_CODEC, OpenOmegaLaserScreenMessage::handleData);
	}
}
