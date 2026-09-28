package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class AdrenalineBladeDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lWHILE HELD" + "\n" + "Gain\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"))
				+ "%\u00A7b Extra damage \u00A7fand\u00A7b Speed boost\u00A7f " + "\n" + "for every \u00A7c" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))
				+ " \u00A7cHP\u00A7f missing";
	}
}