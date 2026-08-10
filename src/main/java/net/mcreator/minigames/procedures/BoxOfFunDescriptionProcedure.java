package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BoxOfFunDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Grant a random\u00A7b status effect\u00A7f to all\u00A7b teammates" + "\n" + "with a random level between "
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))) + "\u00A7f and \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2")) + "\n" + "\u00A7fand a random duration between "
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20)) + "\u00A7f and "
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2") / 20)) + "s" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}