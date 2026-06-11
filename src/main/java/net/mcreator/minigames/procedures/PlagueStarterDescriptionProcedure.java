package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class PlagueStarterDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Create an area where all\u00A7b status effects\u00A7f of" + "\n" + "\u00A7cmonsters\u00A7f are shared with others in that area" + "\n"
				+ ("\u00A7a" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20)) + "s Cooldown " + "\n"
				+ ("\u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20)) + "s Duration";
	}
}