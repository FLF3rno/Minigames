package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModAttributes;
public class BlankDaggerDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		ItemAttributeModifiers modifiers = itemstack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		double value = modifiers.modifiers().stream().filter(entry -> entry.slot().test(EquipmentSlot.MAINHAND) && entry.attribute().is(MinigamesModAttributes.COINS_ON_KILL))
				.mapToDouble(entry -> entry.modifier().amount()).findFirst().orElse(0.0d);
		return "" + "\n" + "\u00A76\u00A7lPASSIVE ABILITY" + "\n" + "Gain\u00A72 "
				+ new java.text.DecimalFormat("#.##").format(value)
				+ "%\u00A7f more\u00A76 coins\u00A7f from killing monsters.";
	}
}
