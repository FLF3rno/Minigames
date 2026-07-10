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

import net.mcreator.minigames.procedures.SpleefSettingsChosenProcedure;
import net.mcreator.minigames.procedures.OpenMapSettingsSpleefProcedure;
import net.mcreator.minigames.procedures.OpenCustomizationScreenProcedure;
import net.mcreator.minigames.procedures.GoBackProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record MinigameGUISpleefButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<MinigameGUISpleefButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "minigame_gui_spleef_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MinigameGUISpleefButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, MinigameGUISpleefButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new MinigameGUISpleefButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<MinigameGUISpleefButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final MinigameGUISpleefButtonMessage message, final IPayloadContext context) {
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

			SpleefSettingsChosenProcedure.execute(world, entity);
		}
		if (buttonID == 1) {

			OpenMapSettingsSpleefProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 2) {

			GoBackProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 3) {

			OpenCustomizationScreenProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(MinigameGUISpleefButtonMessage.TYPE, MinigameGUISpleefButtonMessage.STREAM_CODEC, MinigameGUISpleefButtonMessage::handleData);
	}
}