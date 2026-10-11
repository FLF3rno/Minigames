package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class QuickdrawCrossbowDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON SHIFT RIGHT CLICK" + "\n" + "\u00A7eLoad\u00A7f one \u00A7earrow\u00A7f, up to \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"))
				+ "\n" + "\n" + "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\u00A7eShoot\u00A7f one\u00A7e arrow";
	}
}