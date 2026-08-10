package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class HumanCannonballDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Become the next\u00A7e projectile\u00A7f you shoot" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}