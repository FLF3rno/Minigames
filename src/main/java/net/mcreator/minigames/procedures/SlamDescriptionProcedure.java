package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SlamDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Slam yourself to the ground and launch" + "\n" + "nearby \u00A7cmonsters\u00A7f and\u00A7b teammates\u00A7f in the air" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}