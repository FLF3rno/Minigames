package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BlankLongswordDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON HIT" + "\n" + "\u00A7eStun\u00A7f monsters for\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") / 20)
				+ "s\u00A7f if they are full HP";
	}
}