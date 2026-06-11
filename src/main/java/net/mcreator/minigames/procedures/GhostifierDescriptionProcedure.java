package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class GhostifierDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Gain\u00A79 Phantom\u00A7f and\u00A7b Blessed\u00A7f for "
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20)) + "s" + "\n"
				+ ("\u00A7a" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20)) + "s Cooldown ";
	}
}