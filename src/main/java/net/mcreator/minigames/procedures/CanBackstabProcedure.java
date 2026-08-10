package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

import net.mcreator.minigames.init.MinigamesModItems;

public class CanBackstabProcedure {
	public static boolean execute(ItemStack item) {
		return MinigamesModItems.SILENT_ASSASSIN.get() == item.getItem();
	}
}