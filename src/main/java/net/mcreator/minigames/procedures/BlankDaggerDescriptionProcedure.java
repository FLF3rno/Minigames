package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BlankDaggerDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lPASSIVE ABILITY" + "\n" + "Gain\u00A72 " + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:coins_on_kill"))
				+ "%\u00A7f more\u00A76 coins\u00A7f from killing monsters.";
	}
}