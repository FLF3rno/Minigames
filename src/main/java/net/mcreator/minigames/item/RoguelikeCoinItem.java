package net.mcreator.minigames.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class RoguelikeCoinItem extends Item {
	public RoguelikeCoinItem(Item.Properties properties) {
		super(properties.rarity(Rarity.EPIC));
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}