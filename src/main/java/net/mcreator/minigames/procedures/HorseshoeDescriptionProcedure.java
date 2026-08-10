package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class HorseshoeDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Gain \u00A7aLuck \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "\u00A7f for\u00A72 "
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s" + "\n" + "\n" + ItemUsesDescriptionProcedure.execute(itemstack);
	}
}