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

import net.mcreator.minigames.procedures.AchievementInitiateRerollProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record RerollAchievementMessage(String extradata) implements CustomPacketPayload {
	public static final Type<RerollAchievementMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "reroll_achievement"));
	public static final StreamCodec<RegistryFriendlyByteBuf, RerollAchievementMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, RerollAchievementMessage message) -> {
		buffer.writeUtf(message.extradata);
	}, (RegistryFriendlyByteBuf buffer) -> new RerollAchievementMessage(buffer.readUtf()));

	@Override
	public Type<RerollAchievementMessage> type() {
		return TYPE;
	}

	public static void handleData(final RerollAchievementMessage message, final IPayloadContext context) {
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

				AchievementInitiateRerollProcedure.execute(world, x, y, z);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(RerollAchievementMessage.TYPE, RerollAchievementMessage.STREAM_CODEC, RerollAchievementMessage::handleData);
	}
}