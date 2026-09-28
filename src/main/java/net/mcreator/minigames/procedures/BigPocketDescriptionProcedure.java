package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BigPocketDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A7c\u00A7lUNUSABLE" + "\n" + "\n" + "Gain \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "\u00A76 backpack \u00A7fslots" + "\n" + "\n"
				+ "\u00A7c\u00A7lCANNOT BE PLACED IN THE BACKPACK";
	}
}