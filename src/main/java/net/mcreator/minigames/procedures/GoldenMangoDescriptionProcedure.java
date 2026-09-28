package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class GoldenMangoDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN CONSUMED" + "\n" + "\u00A7aHeal\u00A7f all nearby\u00A7b players " + ("\u00A7c" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"))) + " HP\u00A7f"
				+ "\n" + "Grant them \u00A76Golden\u00A7f for \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2") / 20) + "s" + "\n" + "Grant them \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "% \u00A7bDamage Resistance\u00A7f for\u00A72 "
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}