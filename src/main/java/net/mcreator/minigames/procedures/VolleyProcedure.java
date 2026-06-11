package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class VolleyProcedure {
	public static String execute(ItemStack itemstack) {
		return "\n" + "\u00A76\u00A7lON RIGHT CLICK" + "\n" + "Summon and launch a big, slow bomb foreward" + "\n"
				+ ("\u00A7a" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown") / 20)) + "s Cooldown " + "\n" + "\n" + "\u00A7d\u00A7lHITTING THE BOMB" + "\n"
				+ "\u00A76\u00A7l    WITH A PROJECTILE" + ("     Detonates instantly dealing \u00A72" + new java.text.DecimalFormat("##.#").format(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"))) + "% \u00A7fmore damage"
				+ "\n" + "\n" + "\u00A76\u00A7l    WITH A MELEE ATTACK" + "\n" + "     Knock the bomb forward with increased speed and " + "\n"
				+ ("     \u00A72" + new java.text.DecimalFormat("##.#").format(Math.round(GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") / 2.5))) + "%\u00A7f more damage";
	}
}