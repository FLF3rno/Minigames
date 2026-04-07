package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.SectionPos;

import net.mcreator.minigames.procedures.*;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record CustomizeGUIButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<CustomizeGUIButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "customize_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CustomizeGUIButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CustomizeGUIButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new CustomizeGUIButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<CustomizeGUIButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final CustomizeGUIButtonMessage message, final IPayloadContext context) {
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

			SetNameWhiteProcedure.execute(entity);
		}
		if (buttonID == 1) {

			SetNameAquaProcedure.execute(entity);
		}
		if (buttonID == 2) {

			SetNameDarkAquaProcedure.execute(entity);
		}
		if (buttonID == 3) {

			SetNameBlueProcedure.execute(entity);
		}
		if (buttonID == 4) {

			SetNameDarkBlueProcedure.execute(entity);
		}
		if (buttonID == 5) {

			SetNameGreenProcedure.execute(entity);
		}
		if (buttonID == 6) {

			SetNameDarkGreenProcedure.execute(entity);
		}
		if (buttonID == 7) {

			SetNameLightPurpleProcedure.execute(entity);
		}
		if (buttonID == 8) {

			SetNameDarkPurpleProcedure.execute(entity);
		}
		if (buttonID == 9) {

			SetNameRedProcedure.execute(entity);
		}
		if (buttonID == 10) {

			SetNameDarkRedProcedure.execute(entity);
		}
		if (buttonID == 11) {

			SetNameYellowProcedure.execute(entity);
		}
		if (buttonID == 12) {

			SetNameGoldProcedure.execute(entity);
		}
		if (buttonID == 13) {

			GoBackProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(CustomizeGUIButtonMessage.TYPE, CustomizeGUIButtonMessage.STREAM_CODEC, CustomizeGUIButtonMessage::handleData);
	}
}