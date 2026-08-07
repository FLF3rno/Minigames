package net.mcreator.minigames.world.inventory;

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
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;

	public DungeonInventoryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(MinigamesModMenus.DUNGEON_INVENTORY.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
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

	private void addDynamicPlayerSlots(
			Inventory inventory,
			int playerSlots
	) {
		final int SLOT_SIZE = 18;
		final int SLOT_SPACING = 22;

		// width of the visible main inventory (minimum 5 columns)
		int visibleColumns = Math.max(5, playerSlots);
		int mainWidth = visibleColumns * SLOT_SPACING + 8;

		// hotbar is centered inside the main panel
		int hotbarWidth = playerSlots * SLOT_SIZE;

		int startX = (mainWidth - hotbarWidth) / 2 + 1;
		int y = 88 + 1;

		for (int i = 0; i < playerSlots; i++) {
			addSlot(new HotbarSlot(
					inventory,
					i,
					startX + i * SLOT_SIZE,
					y
			));
		}
	}

	private void addDynamicBackpackSlots(
			Inventory inventory,
			int backpackSlots
	) {
		final int SLOT_SIZE = 18;
		final int SLOT_SPACING = 22;
		final int rows = 3;

		int columns = Math.max(1, (int)Math.ceil(backpackSlots / 3.0));

		int backpackWidth = columns * SLOT_SPACING + 8;

		int gridWidth = columns * SLOT_SPACING - (SLOT_SPACING - SLOT_SIZE);
		int gridHeight = rows * SLOT_SPACING - (SLOT_SPACING - SLOT_SIZE);

		int visibleColumns = Math.max(
				5,
				(int)inventory.player.getData(
						MinigamesModVariables.PLAYER_VARIABLES
				).playerSlots
		);

		int mainWidth = visibleColumns * SLOT_SPACING + 8;

		int panelX = mainWidth + 10;

		int startX =
				panelX
						+ 4
						+ ((backpackWidth - 8) - gridWidth) / 2;

		int startY =
				4
						+ ((112 - 8) - gridHeight) / 2;

		for (int i = 0; i < backpackSlots; i++) {

			int column = i / rows;
			int row = i % rows;

			addSlot(new Slot(
					inventory,
					9 + i,
					startX + column * SLOT_SPACING + 1,
					startY + row * SLOT_SPACING + 1
			));
		}
	}

	private void addRelicSlots(Inventory inventory) {

		addSlot(new RelicSlot(
				inventory,
				34,
				71,
				25
		));

		addSlot(new RelicSlot(
				inventory,
				35,
				71,
				43
		));
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
