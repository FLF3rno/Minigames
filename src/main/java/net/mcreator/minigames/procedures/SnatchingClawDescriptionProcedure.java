package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SnatchingClawDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\u00A7d\u00A7l   HOVERING A PEDESTAL ITEM" + "\n" + "    Steal a\u00A76 pedestal item\u00A7f, making" + "\n" + "    the other pedestals not\u00A7c disappear" + "\n" + "\n"
				+ ItemUsesDescriptionProcedure.execute(itemstack);
	}
}