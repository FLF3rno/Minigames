package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class DungeonItemAccess {
	public static final TagKey<Item> DUNGEON_WARRIOR = itemTag("minigames:dungeon_warrior");
	public static final TagKey<Item> DUNGEON_THIEF = itemTag("minigames:dungeon_thief");
	public static final TagKey<Item> DUNGEON_SUPPORT = itemTag("minigames:dungeon_support");
	public static final TagKey<Item> DUNGEON_MAGE = itemTag("minigames:dungeon_mage");
	public static final TagKey<Item> DUNGEON_TYPE_RELIC = itemTag("minigames:dungeon_type_relic");
	public static final TagKey<Item> DUNGEON_TYPE_WEAPON = itemTag("minigames:dungeon_type_weapon");
	public static final TagKey<Item> HOTBAR_LOCKED = itemTag("minigames:hotbar_locked");

	private DungeonItemAccess() {
	}

	public static boolean isHotbarLocked(ItemStack stack) {
		return stack != null && !stack.isEmpty() && stack.is(HOTBAR_LOCKED);
	}

	public static boolean isDungeonItem(ItemStack stack) {
		return stack.is(DUNGEON_WARRIOR) || stack.is(DUNGEON_THIEF) || stack.is(DUNGEON_SUPPORT) || stack.is(DUNGEON_MAGE);
	}

	public static boolean isDungeonWeapon(ItemStack stack) {
		if (stack.is(DUNGEON_TYPE_WEAPON)) {
			return true;
		}
		Item item = stack.getItem();
		return item == net.mcreator.minigames.init.MinigamesModItems.BLANK_SWORD.get()
				|| item == net.mcreator.minigames.init.MinigamesModItems.BLANK_LONG_SWORD.get()
				|| item == net.mcreator.minigames.init.MinigamesModItems.BLANK_DAGGER.get();
	}

	public static boolean isRelic(ItemStack stack) {
		return stack.is(DUNGEON_TYPE_RELIC);
	}

	public static boolean canClassPickUp(ItemStack stack, String classDungeon) {
		String normalizedClass = classDungeon == null ? "" : classDungeon.trim().toLowerCase();
		if (stack.is(DUNGEON_THIEF)) {
			return normalizedClass.equals("thief");
		}
		if (stack.is(DUNGEON_WARRIOR)) {
			return normalizedClass.equals("warrior") || normalizedClass.equals("thief") && isStolen(stack);
		}
		if (stack.is(DUNGEON_SUPPORT)) {
			return normalizedClass.equals("support") || normalizedClass.equals("thief") && isStolen(stack);
		}
		if (stack.is(DUNGEON_MAGE)) {
			return normalizedClass.equals("mage") || normalizedClass.equals("thief") && isStolen(stack);
		}
		return true;
	}

	public static ItemStack applyDefaultDungeonNBT(ItemStack item) {
		if (item == null || item.isEmpty()) {
			return item;
		}
		Item rawItem = item.getItem();
		if (rawItem == MinigamesModItems.BLACKSMITH_HAMMER.get()) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("isForged", true));
		}
		if (rawItem == MinigamesModItems.GHOSTIFIER.get() ||
				rawItem == MinigamesModItems.PHASE_CLOAK.get()) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("phantom", true));
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("blessed", true));
		}
		if (rawItem == MinigamesModItems.SILENT_ASSASSIN.get()) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("bleed", true));
		}
		if (rawItem == MinigamesModItems.HORSESHOE.get()) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("luck", true));
		}
		if (rawItem == MinigamesModItems.MIDAS_LONGSWORD.get() ||
				rawItem == MinigamesModItems.GOLDEN_MANGO.get() ) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("golden", true));
		}
		if (rawItem == MinigamesModItems.OBSIDIAN_IDLE.get()) {
			CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("obsidianification", true));
		}
		return item;
	}

	public static boolean isStolen(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("stolen", false);
	}

	public static boolean isGlitched(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("glitched", false);
	}

	public static int Glitched(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("glitched", 0);
	}

	public static boolean isForged(ItemStack stack) {
		return isForged(stack, null);
	}

	public static boolean isForged(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
		return Forged(stack, entity) > 0 || stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("isForged", false);
	}

	public static int Forged(ItemStack stack) {
		return Forged(stack, null);
	}

	public static int Forged(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
		int base = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("forged", 0);
		net.minecraft.world.entity.LivingEntity target = entity;
		if (target == null) {
			target = MinigamesMod.clientPlayer();
		}
		int extra = 0;
		if (target != null) {
			ItemStack hammer = net.mcreator.minigames.procedures.CheckRelicProcedure.getRelicStack(target, new ItemStack(net.mcreator.minigames.init.MinigamesModItems.BLACKSMITH_HAMMER.get()));
			if (!hammer.isEmpty()) {
				extra += (int) Math.round(net.mcreator.minigames.procedures.GetItemAttributeProcedure.getBaseAttribute(hammer, "minigames:extra_damage"));
			}
		}
		return base + extra;
	}

	public static boolean hasPhantom(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("phantom", false);
	}

	public static boolean hasBleed(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("bleed", false);
	}

	public static boolean hasLuck(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("luck", false);
	}

	public static boolean hasBlessed(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("blessed", false);
	}

	public static boolean hasGolden(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("golden", false);
	}

	public static boolean hasObsidianification(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("obsidianification", false);
	}

	public static int getBackpackBonus(Player player) {
		if (player == null) {
			return 0;
		}
		int bonus = 0;
		Inventory inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() == MinigamesModItems.BIG_POCKET.get()) {
				double potency = net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(stack, "minigames:effect_potency");
				if (potency <= 0) {
					potency = 3.0;
				}
				bonus += (int) Math.round(potency);
			}
		}
		if (player.containerMenu != null) {
			ItemStack carried = player.containerMenu.getCarried();
			if (!carried.isEmpty() && carried.getItem() == MinigamesModItems.BIG_POCKET.get()) {
				double potency = net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(carried, "minigames:effect_potency");
				if (potency <= 0) {
					potency = 3.0;
				}
				bonus += (int) Math.round(potency);
			}
		}
		return bonus;
	}

	public static int getBackpackSlots(Player player) {
		Player target = player;
		if (target == null && MinigamesMod.clientPlayer() != null) {
			target = MinigamesMod.clientPlayer();
		}
		if (target == null) {
			return 0;
		}
		int base = (int) target.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots;
		int bonus = getBackpackBonus(target);
		return Math.max(0, Math.min(25, base + bonus));
	}

	public static boolean hasDungeonClass(Player player) {
		if (player == null) {
			return false;
		}
		String classDungeon = player.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		String normalizedClass = classDungeon == null ? "" : classDungeon.trim().toLowerCase();
		return "warrior".equals(normalizedClass) || "support".equals(normalizedClass) || "thief".equals(normalizedClass) || "mage".equals(normalizedClass);
	}

	public static boolean isDungeonInventoryActive(Player player) {
		if (player == null || player.level() == null) {
			return false;
		}
		if (!hasDungeonClass(player)) {
			return false;
		}
		boolean inDungeon = MinigamesModVariables.MapVariables.get(player.level()).playingDungeons;
		int baseBackpack = (int) player.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots;
		return inDungeon || baseBackpack > 0;
	}

	public static void dropUnavailableBackpackSlots(Player player) {
		if (player == null || player.level().isClientSide() || !isDungeonInventoryActive(player)) {
			return;
		}
		Inventory inventory = player.getInventory();
		int backpackSlots = getBackpackSlots(player);
		boolean dropped = false;
		for (int i = 9 + backpackSlots; i <= 33; i++) {
			ItemStack stack = inventory.getItem(i);
			if (!stack.isEmpty()) {
				ItemStack toDrop = stack.copy();
				inventory.setItem(i, ItemStack.EMPTY);
				player.drop(toDrop, true);
				dropped = true;
			}
		}
		if (dropped && player.containerMenu != null) {
			player.containerMenu.broadcastChanges();
		}
	}

	private static TagKey<Item> itemTag(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}
}
