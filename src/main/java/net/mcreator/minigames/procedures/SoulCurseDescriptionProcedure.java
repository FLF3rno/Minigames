package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class SoulCurseDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\n" + "Swap \u00A7ds\u00A7eo\u00A7au\u00A7cl\u00A7bs\u00A7f with a hovered\u00A7b teammate" + "\n" + CooldownDescriptionProcedure.execute(itemstack) + "\n"
				+ ItemUsesDescriptionProcedure.execute(itemstack);
	}
}