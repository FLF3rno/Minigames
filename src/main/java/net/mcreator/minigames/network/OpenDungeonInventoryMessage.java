package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.world.inventory.DungeonInventoryMenu;
import net.mcreator.minigames.MinigamesMod;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

@EventBusSubscriber
public record OpenDungeonInventoryMessage() implements CustomPacketPayload {
	public static final Type<OpenDungeonInventoryMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "open_dungeon_inventory"));
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenDungeonInventoryMessage> STREAM_CODEC = StreamCodec.unit(new OpenDungeonInventoryMessage());

	@Override
	public Type<OpenDungeonInventoryMessage> type() {
		return TYPE;
	}

	public static void handleData(final OpenDungeonInventoryMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					String classDungeon = serverPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
					String normalizedClass = classDungeon == null ? "" : classDungeon.trim().toLowerCase();
					if (!("warrior".equals(normalizedClass) || "support".equals(normalizedClass) || "thief".equals(normalizedClass) || "mage".equals(normalizedClass))) {
						return;
					}
					BlockPos pos = serverPlayer.blockPosition();
					serverPlayer.openMenu(new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("DungeonInventory");
						}

						@Override
						public boolean shouldTriggerClientSideContainerClosingOnOpen() {
							return false;
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new DungeonInventoryMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
						}
					}, pos);
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(OpenDungeonInventoryMessage.TYPE, OpenDungeonInventoryMessage.STREAM_CODEC, OpenDungeonInventoryMessage::handleData);
	}
}
