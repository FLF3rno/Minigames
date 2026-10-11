package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class PotionExplotionDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\u00A76Explode\u00A7f all\u00A7c monsters\u00A7f with\u00A7b status effects" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}