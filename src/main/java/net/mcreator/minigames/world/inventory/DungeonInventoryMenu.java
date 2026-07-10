package net.mcreator.minigames.world.inventory;

import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.IItemHandler;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerInput;
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
	public void clicked(int slotId, int button, ContainerInput clickType, Player player) {
		if (isInCombat()) {
			if (clickType == ContainerInput.THROW) {
				if (slotId >= 0 && slotId < this.slots.size()) {
					Slot slot = this.slots.get(slotId);
					if (DungeonItemAccess.isDungeonItem(slot.getItem())) {
						return;
					}
				}
			}
			if (clickType == ContainerInput.PICKUP || clickType == ContainerInput.SWAP || clickType == ContainerInput.QUICK_MOVE || clickType == ContainerInput.QUICK_CRAFT) {
				if (slotId >= 0 && slotId < this.slots.size()) {
					Slot slot = this.slots.get(slotId);
					int containerSlot = slot.getContainerSlot();
					boolean backpackSlot = isBackpackSlot(containerSlot);
					boolean relicSlot = isRelicSlot(containerSlot);
					ItemStack carried = this.getCarried();
					ItemStack slotStack = slot.getItem();
					boolean carriedIsDungeonClassItem = DungeonItemAccess.isDungeonItem(carried);
					boolean slotIsDungeonClassItem = DungeonItemAccess.isDungeonItem(slotStack);
					if ((backpackSlot && DungeonItemAccess.isRelic(carried)) || (backpackSlot && DungeonItemAccess.isRelic(slotStack)) || (relicSlot && DungeonItemAccess.isRelic(slotStack))) {
						return;
					}
					if (backpackSlot && (carriedIsDungeonClassItem || slotIsDungeonClassItem)) {
						return;
					}
				} else {
					if (clickType == ContainerInput.QUICK_CRAFT && DungeonItemAccess.isRelic(this.getCarried())) {
						return;
					}
					if (slotId == -999 && !this.getCarried().isEmpty() && DungeonItemAccess.isDungeonItem(this.getCarried())) {
						return;
					}
				}
			}
		}
		if (clickType == ContainerInput.SWAP && slotId >= 0 && slotId < this.slots.size()) {
			Slot slot = this.slots.get(slotId);
			int containerSlot = slot.getContainerSlot();
			if (containerSlot == LEFT_RELIC_SLOT_INDEX || containerSlot == RIGHT_RELIC_SLOT_INDEX) {
				return;
			}
		}
		super.clicked(slotId, button, clickType, player);
	}

	private boolean isInCombat() {
		return MinigamesModVariables.MapVariables.get(this.world).inCombat;
	}

	private static boolean isRelicSlot(int containerSlot) {
		return containerSlot == LEFT_RELIC_SLOT_INDEX || containerSlot == RIGHT_RELIC_SLOT_INDEX;
	}

	private static boolean isBackpackSlot(int containerSlot) {
		return containerSlot >= 9 && containerSlot <= 33;
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
