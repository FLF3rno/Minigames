package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class WirelessCauldronDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN YOU OBTAIN A STATUS EFFECT" + "\n" + "Give all\u00A7b teammates\u00A7f that\u00A7b status effect\u00A7f for \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s";
	}
}