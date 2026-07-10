package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ReactionTimeDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lWHEN YOU TAKE DAMAGE" + "\n" + "Gain\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "%\u00A7f extra\u00A7c damage\u00A7f for\u00A72 "
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s";
	}
}