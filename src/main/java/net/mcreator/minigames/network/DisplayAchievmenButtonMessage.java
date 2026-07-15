package net.mcreator.minigames.network;

import net.mcreator.minigames.procedures.AchievementReadyUpProcedure;
import net.mcreator.minigames.procedures.AchievementRerollProcedure;
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
public record DisplayAchievmenButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<DisplayAchievmenButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "display_achievmen_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, DisplayAchievmenButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, DisplayAchievmenButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new DisplayAchievmenButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<DisplayAchievmenButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final DisplayAchievmenButtonMessage message, final IPayloadContext context) {
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

			AchievementReadyUpProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			AchievementRerollProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(DisplayAchievmenButtonMessage.TYPE, DisplayAchievmenButtonMessage.STREAM_CODEC, DisplayAchievmenButtonMessage::handleData);
	}
}