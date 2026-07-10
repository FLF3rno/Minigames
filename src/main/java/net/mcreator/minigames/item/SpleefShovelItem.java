package net.mcreator.minigames.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class SpleefShovelItem extends Item {
	public SpleefShovelItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		return 100000f;
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}