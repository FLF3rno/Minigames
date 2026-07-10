package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ClockDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\u00A79Slow\u00A7f down\u00A7b time\u00A7f to " + ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")))
				+ "\u00A72%\u00A7f for " + ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20)) + "\u00A72s\u00A7f," + "\n"
				+ "then\u00A7b speed\u00A7f up\u00A7b time\u00A7f by " + ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))) + "\u00A72%\u00A7f for "
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2") / 20)) + "\u00A72s" + "\n"
				+ ("\u00A7a" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20)) + "s Cooldown ";
	}
}