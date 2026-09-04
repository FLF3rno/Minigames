package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.procedures.UpdateWorldRendererProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record UpdateWorldRendererPMessage(String extradata) implements CustomPacketPayload {
	public static final Type<UpdateWorldRendererPMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "update_world_renderer_p"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateWorldRendererPMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, UpdateWorldRendererPMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new UpdateWorldRendererPMessage(buffer.readUtf()));

	@Override
	public Type<UpdateWorldRendererPMessage> type() {
		return TYPE;
	}

	public static void handleData(final UpdateWorldRendererPMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;

				UpdateWorldRendererProcedure.execute();
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(UpdateWorldRendererPMessage.TYPE, UpdateWorldRendererPMessage.STREAM_CODEC, UpdateWorldRendererPMessage::handleData);
	}
}