package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class ReturnRelicItemstackProcedure {
	public static ItemStack execute(Entity target, ItemStack item) {
		if (target == null)
			return ItemStack.EMPTY;
		if ((target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler0 ? _modHandler0.getStackInSlot(34).copy() : ItemStack.EMPTY).getItem() == item.getItem()) {
			return target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler2 ? _modHandler2.getStackInSlot(34).copy() : ItemStack.EMPTY;
		} else if ((target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler3 ? _modHandler3.getStackInSlot(35).copy() : ItemStack.EMPTY).getItem() == item.getItem()) {
			return target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler5 ? _modHandler5.getStackInSlot(35).copy() : ItemStack.EMPTY;
		}
		return new ItemStack(Blocks.AIR);
	}
}