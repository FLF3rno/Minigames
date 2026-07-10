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

import net.mcreator.minigames.procedures.SelectSpleefProcedure;
import net.mcreator.minigames.procedures.ActivateSteampunkProcedure;
import net.mcreator.minigames.procedures.ActivateSolarSystemProcedure;
import net.mcreator.minigames.procedures.ActivateChristmasProcedure;
import net.mcreator.minigames.procedures.ActivateBalloonsProcedure;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record MapGUISpleefButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {
	public static final Type<MapGUISpleefButtonMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "map_gui_spleef_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MapGUISpleefButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, MapGUISpleefButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new MapGUISpleefButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<MapGUISpleefButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final MapGUISpleefButtonMessage message, final IPayloadContext context) {
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

			SelectSpleefProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {

			ActivateBalloonsProcedure.execute(world);
		}
		if (buttonID == 2) {

			ActivateSolarSystemProcedure.execute(world);
		}
		if (buttonID == 3) {

			ActivateSteampunkProcedure.execute(world);
		}
		if (buttonID == 4) {

			ActivateChristmasProcedure.execute(world);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(MapGUISpleefButtonMessage.TYPE, MapGUISpleefButtonMessage.STREAM_CODEC, MapGUISpleefButtonMessage::handleData);
	}
}