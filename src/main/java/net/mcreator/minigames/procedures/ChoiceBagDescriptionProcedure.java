package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ChoiceBagDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Obtain a choice between 3 random items" + "\n" + ItemUsesDescriptionProcedure.execute(itemstack);
	}
}