package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class HeartAmuletDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "\u00A72+" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "%\u00A7f incoming and outgoing\u00A7a healing" + "\n" + "\n"
				+ "\u00A76\u00A7lPASSIVE" + "\n" + "\u00A7cCannot heal passively";
	}
}