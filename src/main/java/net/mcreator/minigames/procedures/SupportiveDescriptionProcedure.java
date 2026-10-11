package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SupportiveDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN HITTING A TEAMMATE WITH A PROJECTILE" + "\n" + "Grant that \u00A7bteammate\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "% "
				+ "\u00A7bSpeed Boost\u00A7f for\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "\u00A7fs";
	}
}