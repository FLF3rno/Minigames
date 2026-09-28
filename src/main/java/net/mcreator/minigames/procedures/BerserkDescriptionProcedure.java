package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BerserkDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "For \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s\u00A7f gain \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "% \u00A7eAttack Speed\u00A7f," + "\n" + "\u00A7c"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2")) + "%\u00A7c Attack Damage" + "\n" + "and \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "%\u00A7a Lifesteal" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}