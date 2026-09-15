package net.mcreator.minigames.network;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.procedures.CheckRelicProcedure;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import net.mcreator.minigames.procedures.ItemPickedUpDungeonProcedure;
import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record DungeonItemPickupMessage(int entityId) implements CustomPacketPayload {
	public static final Type<DungeonItemPickupMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "dungeon_item_pickup"));
	public static final StreamCodec<RegistryFriendlyByteBuf, DungeonItemPickupMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, DungeonItemPickupMessage message) -> {
		buffer.writeInt(message.entityId);
	}, buffer -> new DungeonItemPickupMessage(buffer.readInt()));

	@Override
	public Type<DungeonItemPickupMessage> type() {
		return TYPE;
	}

	public static void handleData(final DungeonItemPickupMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					tryPickup(serverPlayer, message.entityId);
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	private static void tryPickup(ServerPlayer player, int entityId) {
		Entity entity = player.level().getEntity(entityId);
		if (!(entity instanceof ItemEntity itemEntity)) {
			return;
		}
		if (!itemEntity.isAlive() || player.distanceToSqr(itemEntity) > 64.0D) {
			return;
		}

		ItemStack entityStack = itemEntity.getItem();
		tryPickupStack(player, entityStack, itemEntity);
	}

	public static void tryPickupStack(ServerPlayer player, ItemStack entityStack, ItemEntity itemEntity) {
		if (!DungeonItemAccess.isDungeonItem(entityStack)) {
			return;
		}

		String classDungeon = player.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		if (!DungeonItemAccess.canClassPickUp(entityStack, classDungeon)) {
			return;
		}

		boolean isRelic = DungeonItemAccess.isRelic(entityStack);

		double px = itemEntity != null ? itemEntity.getX() : player.getX();
		double py = itemEntity != null ? itemEntity.getY() : player.getY();
		double pz = itemEntity != null ? itemEntity.getZ() : player.getZ();

		ItemPickedUpDungeonProcedure.execute(
			player.level(),
			px, 
			py, 
			pz, 
			player,
			entityStack
		);

		int inserted;

		if (isRelic) {
			inserted = tryInsertRelic(player.getInventory(), entityStack.copy());
			if (inserted <= 0) {
				if (itemEntity != null) {
					player.sendSystemMessage(Component.literal("§cRELIC SLOTS FULL"), true);
					return;
				} else if (!insertIntoBackpackSlot(player, entityStack)) {
					player.drop(entityStack, false);
					inserted = entityStack.getCount();
				} else {
					inserted = entityStack.getCount();
				}
			}
		} else {
			if (isInventoryFull(player)) {
				if (itemEntity != null) {
					player.sendSystemMessage(Component.literal("§cInventory is full!"), true);
					return;
				} else if (!insertIntoAvailableSlot(player, entityStack)) {
					player.drop(entityStack, false);
					inserted = entityStack.getCount();
				} else {
					inserted = entityStack.getCount();
				}
			} else {
				ItemStack remaining = entityStack.copy();
				player.getInventory().add(remaining);
				inserted = entityStack.getCount() - remaining.getCount();
			}
		}

		if (inserted <= 0) {
			return;
		}

		if (itemEntity != null) {
			if (inserted >= entityStack.getCount()) {
				itemEntity.discard();
			} else {
				entityStack.shrink(inserted);
				itemEntity.setItem(entityStack);
			}
		}

		player.containerMenu.broadcastChanges();

		float pitch = ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F;
		player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
				SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, pitch);
	}

	public static boolean insertIntoBackpackSlot(ServerPlayer player, ItemStack stack) {
		Inventory inv = player.getInventory();
		double backpackSlotsVal = player.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots;
		int backpackSlots = Math.max(0, Math.min(25, (int) backpackSlotsVal));
		for (int i = 0; i < backpackSlots; i++) {
			int slotIndex = 9 + i;
			if (slotIndex < 36 && inv.getItem(slotIndex).isEmpty()) {
				inv.setItem(slotIndex, stack);
				return true;
			}
		}
		return false;
	}

	public static boolean insertIntoAvailableSlot(ServerPlayer player, ItemStack stack) {
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
		if (insertIntoBackpackSlot(player, stack)) {
			return true;
		}

		return false;
	}

	private static boolean isInventoryFull(ServerPlayer player) {
		double playerSlots = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
		int nSlots = Math.max(0, Math.min(9, (int) playerSlots));
		for (int i = 0; i < nSlots; i++) {
			if (player.getInventory().getItem(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private static int tryInsertRelic(Inventory inventory, ItemStack stack) {
		if (inventory.getItem(34).isEmpty()) {
			inventory.setItem(34, stack);
			return stack.getCount();
		}
		if (inventory.getItem(35).isEmpty()) {
			inventory.setItem(35, stack);
			return stack.getCount();
		}
		return 0;
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(DungeonItemPickupMessage.TYPE, DungeonItemPickupMessage.STREAM_CODEC, DungeonItemPickupMessage::handleData);
	}
}



