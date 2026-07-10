package net.mcreator.minigames;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class DungeonItemAccess {
	public static final TagKey<Item> DUNGEON_WARRIOR = itemTag("minigames:dungeon_warrior");
	public static final TagKey<Item> DUNGEON_THIEF = itemTag("minigames:dungeon_thief");
	public static final TagKey<Item> DUNGEON_SUPPORT = itemTag("minigames:dungeon_support");
	public static final TagKey<Item> DUNGEON_MAGE = itemTag("minigames:dungeon_mage");
	public static final TagKey<Item> DUNGEON_TYPE_RELIC = itemTag("minigames:dungeon_type_relic");

	private DungeonItemAccess() {
	}

	public static boolean isDungeonItem(ItemStack stack) {
		return stack.is(DUNGEON_WARRIOR) || stack.is(DUNGEON_THIEF) || stack.is(DUNGEON_SUPPORT) || stack.is(DUNGEON_MAGE);
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
			return normalizedClass.equals("mage");
		}
		return true;
	}

	//CONDITIONS per PROPERTIES
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
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("isForged", false);
	}
	public static int Forged(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("forged", 0);
	}
	//CONDITIONS per EFFECTS
	public static boolean hasPhantom(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("phantom", false);
	}
	public static boolean hasBlessed(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("blessed", false);
	}

	private static TagKey<Item> itemTag(String id) {
		return TagKey.create(Registries.ITEM, ResourceLocation.parse(id));
	}
}
