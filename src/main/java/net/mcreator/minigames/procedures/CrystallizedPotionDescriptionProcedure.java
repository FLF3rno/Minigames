package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class CrystallizedPotionDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN A STATUS EFFECT EXPIRES" + "\n" + "All\u00A7b teammates\u00A7f are healed " + "\n" + "for \u00A7c" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:heal_amount"))
				+ "% Max Health";
	}
}