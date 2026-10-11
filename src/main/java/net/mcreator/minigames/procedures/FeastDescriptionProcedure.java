package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class FeastDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\u00A7cDestroy\u00A7f all nearby\u00A7e projectiles\u00A7f and" + "\n" + "\u00A7dtransform\u00A7f them into orbs that\u00A7a heal \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) + "\u00A7c HP" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}