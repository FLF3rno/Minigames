package net.mcreator.minigames.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record ChoiceBagRewardMessage(String itemId) implements CustomPacketPayload {
	public static final Type<ChoiceBagRewardMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "choice_bag_reward"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ChoiceBagRewardMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, ChoiceBagRewardMessage message) -> buffer.writeUtf(message.itemId),
			(RegistryFriendlyByteBuf buffer) -> new ChoiceBagRewardMessage(buffer.readUtf())
	);

	@Override
	public Type<ChoiceBagRewardMessage> type() {
		return TYPE;
	}

	public static void handleData(final ChoiceBagRewardMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					giveReward(serverPlayer, message.itemId);
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	private static void giveReward(ServerPlayer player, String itemIdStr) {
		Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(itemIdStr));
		if (item == null || item == Items.AIR) {
			return;
		}

		ItemStack stack = new ItemStack(item);
		Inventory inv = player.getInventory();

		boolean isRelic = DungeonItemAccess.isRelic(stack);
		if (isRelic) {
			if (inv.getItem(34).isEmpty()) {
				inv.setItem(34, stack);
			} else if (inv.getItem(35).isEmpty()) {
				inv.setItem(35, stack);
			} else {
				if (!insertIntoAvailableSlot(player, stack)) {
					player.drop(stack, false);
				}
			}
		} else {
			if (!insertIntoAvailableSlot(player, stack)) {
				player.drop(stack, false);
			}
		}

		player.containerMenu.broadcastChanges();
		player.closeContainer();

		player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
	}

	private static boolean insertIntoAvailableSlot(ServerPlayer player, ItemStack stack) {
		Inventory inv = player.getInventory();
		double playerSlotsVal = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
		int hotbarSlots = Math.max(0, Math.min(9, (int) playerSlotsVal));

		// 1. Hotbar first (slots 0 to hotbarSlots - 1)
		for (int i = 0; i < hotbarSlots; i++) {
			if (inv.getItem(i).isEmpty()) {
				inv.setItem(i, stack);
				return true;
			}
		}

		// 2. Backpack slots (slots 9 to 9 + backpackSlots - 1)
		double backpackSlotsVal = player.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots;
		int backpackSlots = Math.max(0, Math.min(27, (int) backpackSlotsVal));
		for (int i = 0; i < backpackSlots; i++) {
			int slotIndex = 9 + i;
			if (slotIndex < 36 && inv.getItem(slotIndex).isEmpty()) {
				inv.setItem(slotIndex, stack);
				return true;
			}
		}

		// 3. Fallback to any slot in 36
		for (int i = 0; i < 36; i++) {
			if (inv.getItem(i).isEmpty()) {
				inv.setItem(i, stack);
				return true;
			}
		}

		return false;
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(ChoiceBagRewardMessage.TYPE, ChoiceBagRewardMessage.STREAM_CODEC, ChoiceBagRewardMessage::handleData);
	}
}
