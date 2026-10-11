package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class ObsidianIdleDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "\n" + "Gain \u00A75Obsidianification\u00A7f for\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s" + "\n"
				+ CooldownDescriptionProcedure.execute(itemstack);
	}
}