package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ClusterBombDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN YOU CREATE AN EXPLOSION" + "\n" + "\u00A7cExplode\u00A7f again with the same" + "\n" + "\u00A7cexplosion\u00A7f at \u00A74"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "%\u00A7f after \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20) + "s";
	}
}