package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class EmptyHeartAmuletDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "\u00A72+" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "%\u00A7f passive\u00A7a healing" + "\n"
				+ "for you andall\u00A7b teammates";
	}
}