package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModItems;

public class RelicUnequippedProcedure {
	public static void execute(Entity entity, ItemStack item) {
		if (entity == null)
			return;
		double num = 0;
		ItemStack modifieditem = ItemStack.EMPTY;
		if (item.getItem() == MinigamesModItems.BLACKSMITH_HAMMER.get()) {
			num = 0;
			for (int index0 = 0; index0 < 9; index0++) {
				modifieditem = (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler1 ? _modHandler1.getStackInSlot((int) num).copy() : ItemStack.EMPTY).copy();
				{
					final String _tagName = "forged";
					final double _tagValue = ((entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler2 ? _modHandler2.getStackInSlot((int) num).copy() : ItemStack.EMPTY)
							.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("forged", 0) - 10);
					CustomData.update(DataComponents.CUSTOM_DATA, modifieditem, tag -> tag.putDouble(_tagName, _tagValue));
				}
				if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler) {
					ItemStack _setstack = modifieditem.copy();
					_setstack.setCount(1);
					_modHandler.setStackInSlot((int) num, _setstack);
				}
				num = num + 1;
			}
		}
	}
}