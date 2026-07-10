package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class ReturnRelicItemstackProcedure {
	public static ItemStack execute(Entity target, ItemStack item) {
		if (target == null)
			return ItemStack.EMPTY;
		if ((getEntitySlot(target, 34)).getItem() == item.getItem()) {
			return getEntitySlot(target, 34);
		} else if ((getEntitySlot(target, 35)).getItem() == item.getItem()) {
			return getEntitySlot(target, 35);
		}
		return new ItemStack(Blocks.AIR);
	}

	private static ItemStack getEntitySlot(Entity entity, int slot) {
		if (entity != null) {
			ResourceHandler<ItemResource> resourceHandler = entity.getCapability(Capabilities.Item.ENTITY, null);
			if (resourceHandler != null) {
				return ItemUtil.getStack(resourceHandler, slot);
			}
		}
		return ItemStack.EMPTY;
	}
}