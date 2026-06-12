package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BlacksmithHammerDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "All items gain \u00A76" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "% FORGED";
	}
}