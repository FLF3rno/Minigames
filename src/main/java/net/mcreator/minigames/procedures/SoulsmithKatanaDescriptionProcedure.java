package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SoulsmithKatanaDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON KILL" + "\n" + "\u00A7dRevive\u00A7f the killed\u00A7c monster\u00A7f with \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))
				+ "%\u00A7c HP" + "\n" + "The revived \u00A7cmonster\u00A7f does not grant\u00A76 coins\u00A7f or\u00A76 items";
	}
}