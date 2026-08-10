package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ParasiteScytheDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT" + "\n" + "The next \u00A7cdamage \u00A7ftaken is \u00A7areduced" + "\n" + "\u00A7aby \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "%\u00A7f and\u00A75 dealt\u00A7f over \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s" + "\n" + "\u00A78(1 HP minimum)";
	}
}