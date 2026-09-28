package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ThrustersDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "All\u00A7d dashes\u00A7f from all\u00A7b players" + "\n" + "are \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))
				+ "%\u00A7f stronger";
	}
}