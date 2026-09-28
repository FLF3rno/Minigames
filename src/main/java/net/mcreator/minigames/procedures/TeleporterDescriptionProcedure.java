package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class TeleporterDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Swap your and your\u00A7b teammate's\u00A7d positions" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}