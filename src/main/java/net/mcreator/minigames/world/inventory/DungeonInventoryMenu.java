package net.mcreator.minigames.world.inventory;

import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.IItemHandler;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModMenus;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class DungeonInventoryMenu extends AbstractContainerMenu implements MinigamesModMenus.MenuAccessor {
	private static final int LEFT_RELIC_SLOT_INDEX = 34;
	private static final int RIGHT_RELIC_SLOT_INDEX = 35;
	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 5)
				return null;
			return super.put(key, value);
		}
	};
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private IItemHandler internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;

	public DungeonInventoryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(MinigamesModMenus.DUNGEON_INVENTORY.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new ItemStackHandler(0);
		int playerSlots = Math.max(0, Math.min(9, (int) inv.player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots));
		int backpackSlots = Math.max(0, Math.min(25, (int) inv.player.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots));
		addRelicSlots(inv);
		addDynamicPlayerSlots(inv, playerSlots);
		addDynamicBackpackSlots(inv, backpackSlots);
		BlockPos pos = null;
		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		if (clickType == ClickType.SWAP && slotId >= 0 && slotId < this.slots.size()) {
			Slot slot = this.slots.get(slotId);
			int containerSlot = slot.getContainerSlot();
			if (containerSlot == LEFT_RELIC_SLOT_INDEX || containerSlot == RIGHT_RELIC_SLOT_INDEX) {
				return;
			}
		}
		super.clicked(slotId, button, clickType, player);
	}

	private void addDynamicPlayerSlots(Inventory inventory, int playerSlots) {
		final int centerX = 77;
		final int y = 125;
		final int slotSpacing = 22;
		final int slotInset = 2;
		int startX = centerX - (playerSlots * slotSpacing) / 2;
		for (int i = 0; i < playerSlots; i++) {
			Slot slot = new HotbarSlot(inventory, i, startX + i * slotSpacing + slotInset, y + slotInset);
			this.customSlots.put(this.customSlots.size(), this.addSlot(slot));
		}
	}

	private void addDynamicBackpackSlots(Inventory inventory, int backpackSlots) {
		final int startX = 74;
		final int startY = 44;
		final int maxRowsPerColumn = 3;
		final int slotSpacing = 22;
		final int slotInset = 2;
		int maxSlots = Math.min(backpackSlots, 25);
		for (int i = 0; i < maxSlots; i++) {
			int column = i / maxRowsPerColumn;
			int row = i % maxRowsPerColumn;
			int x = startX + column * slotSpacing;
			int y = startY + row * slotSpacing;
			int inventorySlot = 9 + i;
			Slot slot = new Slot(inventory, inventorySlot, x + slotInset, y + slotInset);
			this.customSlots.put(this.customSlots.size(), this.addSlot(slot));
		}
	}

	private void addRelicSlots(Inventory inventory) {
		final int slotInset = 2;
		Slot leftRelic = new RelicSlot(inventory, 34, 25 + slotInset, 17 + slotInset);
		Slot rightRelic = new RelicSlot(inventory, 35, 49 + slotInset, 17 + slotInset);
		this.customSlots.put(this.customSlots.size(), this.addSlot(leftRelic));
		this.customSlots.put(this.customSlots.size(), this.addSlot(rightRelic));
	}

	private static class HotbarSlot extends Slot {
		public HotbarSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return !DungeonItemAccess.isRelic(stack);
		}
	}

	private static class RelicSlot extends Slot {
		public RelicSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return DungeonItemAccess.isRelic(stack);
		}
	}

	@Override
	public Map<Integer, Slot> getSlots() {
		return Collections.unmodifiableMap(customSlots);
	}

	@Override
	public Map<String, Object> getMenuState() {
		return menuState;
	}
}
