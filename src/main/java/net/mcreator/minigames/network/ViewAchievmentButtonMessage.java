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
import net.minecraft.core.SectionPos;

import net.mcreator.minigames.procedures.RerollAchievementPressedProcedure;
import net.mcreator.minigames.procedures.ReadyUpProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record ViewAchievmentButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<ViewAchievmentButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "view_achievment_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ViewAchievmentButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ViewAchievmentButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ViewAchievmentButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ViewAchievmentButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ViewAchievmentButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> handleButtonAction(context.player(), message.buttonID, message.x, message.y, message.z)).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		// security measure to prevent arbitrary chunk generation
		if (!world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)))
			return;
		if (buttonID == 0) {

			ReadyUpProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			RerollAchievementPressedProcedure.execute(world, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(ViewAchievmentButtonMessage.TYPE, ViewAchievmentButtonMessage.STREAM_CODEC, ViewAchievmentButtonMessage::handleData);
	}
}