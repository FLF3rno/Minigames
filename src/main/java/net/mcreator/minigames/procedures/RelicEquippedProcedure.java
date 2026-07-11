package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModItems;

public class RelicEquippedProcedure {
	public static void execute(Entity entity, ItemStack item) {
		if (entity == null)
			return;
		double num = 0;
		ItemStack modifieditem = ItemStack.EMPTY;
		if (item.getItem() == MinigamesModItems.BLACKSMITH_HAMMER.get()) {
			num = 0;
			for (int index64 = 0; index64 < 6; index64++) {
				modifieditem = (getEntitySlot(entity, (int) num)).copy();
				{
					final String _tagName = "forged";
					final double _tagValue = ((getEntitySlot(entity, (int) num)).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("forged", 0) + 10);
					CustomData.update(DataComponents.CUSTOM_DATA, modifieditem, tag -> tag.putDouble(_tagName, _tagValue));
				}
				if (entity.getCapability(Capabilities.Item.ENTITY, null) instanceof ResourceHandler<ItemResource> _resourceHandler) {
					setStackInSlot(_resourceHandler, (int) num, ItemResource.of(modifieditem), 1);
				}
				num = num + 1;
			}
		}
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

	private static void setStackInSlot(ResourceHandler<ItemResource> handler, int index, ItemResource resource, int amount) {
		try (var tx = Transaction.openRoot()) {
			if (!handler.getResource(index).isEmpty())
				handler.extract(index, handler.getResource(index), handler.getAmountAsInt(index), tx);
			if (!resource.isEmpty() && amount > 0)
				handler.insert(index, resource, amount, tx);
			tx.commit();
		}
	}
}
