package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class BatEssenceDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		return "\u00A76\u00A7lPASSIVE" + "\n" + "Gain complete blindness" + "\n" + "All\u00A7b entities\u00A7f in the room glow" + "\n" + "Deal \u00A72"
				+ new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")) + "%\u00A7f extra\u00A7c damage";
	}
}