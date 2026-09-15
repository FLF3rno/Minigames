package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.AnimationScreenTrigger;

@EventBusSubscriber
public record PlayScreenAnimationMessage(int length, String animationType, float speed) implements CustomPacketPayload {
	public static final Type<PlayScreenAnimationMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "play_screen_animation"));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayScreenAnimationMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, PlayScreenAnimationMessage message) -> {
				buffer.writeInt(message.length());
				buffer.writeUtf(message.animationType());
				buffer.writeFloat(message.speed());
			},
			(RegistryFriendlyByteBuf buffer) -> new PlayScreenAnimationMessage(buffer.readInt(), buffer.readUtf(), buffer.readFloat())
	);

	@Override
	public Type<PlayScreenAnimationMessage> type() {
		return TYPE;
	}

	public static void handleData(final PlayScreenAnimationMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				AnimationScreenTrigger.startAnimation(message.length(), message.animationType(), message.speed());
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void sendToAll(LevelAccessor world, int length, String animationType, float speed) {
		if (world instanceof ServerLevel) {
			PacketDistributor.sendToAllPlayers(new PlayScreenAnimationMessage(length, animationType, speed));
		} else if (world.isClientSide()) {
			AnimationScreenTrigger.startAnimation(length, animationType, speed);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(PlayScreenAnimationMessage.TYPE, PlayScreenAnimationMessage.STREAM_CODEC, PlayScreenAnimationMessage::handleData);
	}
}
