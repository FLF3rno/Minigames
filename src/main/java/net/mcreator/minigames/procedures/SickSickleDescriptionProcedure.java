package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SickSickleDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT" + "\n" + "Apply\u00A75 Decay\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "\u00A7f for \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s" + "\n" + "to the hit\u00A7b entity";
	}
}