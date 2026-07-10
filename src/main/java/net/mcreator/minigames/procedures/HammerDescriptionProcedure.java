package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class HammerDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT" + "\n" + "Apply\u00A72 1\u00A7f stack of\u00A7c strength\u00A7f " + "\n" + "for \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "\u00A72s\u00A7f to the hit\u00A7c monster";
	}
}