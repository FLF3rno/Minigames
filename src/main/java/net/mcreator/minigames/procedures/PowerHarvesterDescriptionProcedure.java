package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class PowerHarvesterDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT - IF THE MONSTER IS STUNNED" + "\n" + "Add and refresh\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"))
				+ "%\u00A7b Extra damage\u00A7f for \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s";
	}
}