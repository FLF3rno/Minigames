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
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 1) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 2) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 3) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 4) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 5) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 6) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 7) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 8) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 9) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 10) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 11) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 12) {
			CustomizeNameColorProcedure.executeDirectColor(world, entity, hsvButtonColor(buttonID));
		}
		if (buttonID == 13) {

			GoBackProcedure.execute(world, x, y, z, entity);
		}
	}

	private static String hsvButtonColor(int buttonID) {
		int steps = 13;
		float hue = (buttonID % steps) / (float) steps;
		int rgb = java.awt.Color.HSBtoRGB(hue, 0.9f, 1.0f) & 0xFFFFFF;
		return String.format("#%06X", rgb);
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(CustomizeGUIButtonMessage.TYPE, CustomizeGUIButtonMessage.STREAM_CODEC, CustomizeGUIButtonMessage::handleData);
	}
}
