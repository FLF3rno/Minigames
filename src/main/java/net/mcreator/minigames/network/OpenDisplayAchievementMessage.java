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

import net.mcreator.minigames.procedures.DisplayAchievementOpenProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record OpenDisplayAchievementMessage(String extradata) implements CustomPacketPayload {
	public static final Type<OpenDisplayAchievementMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "open_display_achievement"));
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenDisplayAchievementMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, OpenDisplayAchievementMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new OpenDisplayAchievementMessage(buffer.readUtf()));

	@Override
	public Type<OpenDisplayAchievementMessage> type() {
		return TYPE;
	}

	public static void handleData(final OpenDisplayAchievementMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				String inboundString = message.extradata;
				if (!world.hasChunkAt(entity.blockPosition()))
					return;

				DisplayAchievementOpenProcedure.execute(world, x, y, z, entity);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(OpenDisplayAchievementMessage.TYPE, OpenDisplayAchievementMessage.STREAM_CODEC, OpenDisplayAchievementMessage::handleData);
	}
}