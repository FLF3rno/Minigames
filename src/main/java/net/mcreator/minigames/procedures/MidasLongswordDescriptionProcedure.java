package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class MidasLongswordDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT" + "\n" + "Apply \u00A76Golden\u00A7f to \u00A7cmonsters" + "\n" + "\u00A7ffor\u00A72 "
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20) + "s\u00A7f if they are full \u00A7cHP";
	}
}