package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class CheckRelicProcedure {
	public static boolean execute(Entity target, ItemStack item) {
		return !getRelicStack(target, item).isEmpty();
	}

	public static ItemStack getRelicStack(Entity target, ItemStack item) {
		if (target == null || item == null || item.isEmpty())
			return ItemStack.EMPTY;
		if (target instanceof Player) {
			ItemStack s34 = getEntitySlot(target, 34);
			if (s34.getItem() == item.getItem()) {
				return s34;
			}
			ItemStack s35 = getEntitySlot(target, 35);
			if (s35.getItem() == item.getItem()) {
				return s35;
			}
		}
		return ItemStack.EMPTY;
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