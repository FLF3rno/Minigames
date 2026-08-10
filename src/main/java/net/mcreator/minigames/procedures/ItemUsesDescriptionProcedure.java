package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ItemUsesDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return ("\u00A7a\u00A7l" + (itemstack.getMaxDamage() - itemstack.getDamageValue())) + " USES LEFT";
	}
}