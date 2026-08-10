package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SilentAssassinDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON BACKSTAB" + "\n" + "Deal \u00A72" + new java.text.DecimalFormat("##").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") * 100) + "% \u00A7adamage \u00A7f" + "\n"
				+ "and inflict \u00A74Bleed \u00A721\u00A7f for \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s";
	}
}