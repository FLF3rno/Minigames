package net.mcreator.minigames.network;

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

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.procedures.PlayIntroCutsceneProcedure;

@EventBusSubscriber
public record PlayIntroCutsceneMessage(String extradata) implements CustomPacketPayload {
	public static final Type<PlayIntroCutsceneMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "play_intro_cutscene"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayIntroCutsceneMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, PlayIntroCutsceneMessage message) -> buffer.writeUtf(message.extradata),
			(RegistryFriendlyByteBuf buffer) -> new PlayIntroCutsceneMessage(buffer.readUtf())
	);

	@Override
	public Type<PlayIntroCutsceneMessage> type() {
		return TYPE;
	}

	public static void handleData(final PlayIntroCutsceneMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				PlayIntroCutsceneProcedure.execute();
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(PlayIntroCutsceneMessage.TYPE, PlayIntroCutsceneMessage.STREAM_CODEC, PlayIntroCutsceneMessage::handleData);
	}
}
