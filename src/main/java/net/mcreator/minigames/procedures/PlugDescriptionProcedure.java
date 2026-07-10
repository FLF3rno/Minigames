package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class PlugDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "Attacks\u00A7e chain\u00A7f to a random nearby\u00A7c monster" + "\n" + "dealing \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "%\u00A7f less\u00A7c damage";
	}
}