package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class DetonatorDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Create an\u00A76 explosion\u00A7f at the hovered " + "\n" + "\u00A7bteammate\u00A7f making them lose \u00A7c"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "\u00A7c HP" + "\n" + CooldownDescriptionProcedure.execute(itemstack);
	}
}