package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class CheckRelicProcedure {
	public static boolean execute(Entity target, ItemStack item) {
		if (target == null)
			return false;
		if ((target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler0 ? _modHandler0.getStackInSlot(34).copy() : ItemStack.EMPTY).getItem() == item.getItem()) {
			return true;
		} else if ((target.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler2 ? _modHandler2.getStackInSlot(35).copy() : ItemStack.EMPTY).getItem() == item.getItem()) {
			return true;
		}
		return false;
	}
}