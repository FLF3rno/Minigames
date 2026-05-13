package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModAttributes;
public class BlankLongswordDescriptionProcedure {
	public static String execute(ItemStack itemstack) {
		ItemAttributeModifiers modifiers = itemstack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		double effectLengthTicks = modifiers.modifiers().stream().filter(entry -> entry.slot().test(EquipmentSlot.MAINHAND) && entry.attribute().is(MinigamesModAttributes.EFFECT_LENGTH))
				.mapToDouble(entry -> entry.modifier().amount()).findFirst().orElse(0.0d);
		double effectLengthSeconds = effectLengthTicks / 20d;
		return "" + "\n" + "\u00A76\u00A7lON HIT" + "\n" + "\u00A7eStun\u00A7f monsters for\u00A72 "
				+ new java.text.DecimalFormat("#.##").format(effectLengthSeconds)
				+ "s\u00A7f if they are full HP";
	}
}
