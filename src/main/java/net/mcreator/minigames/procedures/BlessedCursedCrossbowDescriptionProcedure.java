package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BlessedCursedCrossbowDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lPASSIVE" + "\n" + "Can save\u00A76 ascending\u00A7b teammates" + "\n" + "\n" + "\u00A76\u00A7lON SHOOT" + "Deals \u00A7c" + GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")
				+ " damage\u00A7f to the\u00A7b shooter";
	}
}