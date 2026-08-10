package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class CooldownDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return ("\u00A7a" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20)) + "s Cooldown ";
	}
}