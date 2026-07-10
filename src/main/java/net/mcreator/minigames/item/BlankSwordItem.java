package net.mcreator.minigames.item;

import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

public class BlankSwordItem extends Item {
	public BlankSwordItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant().attributes(
				ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "blank_sword_0"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "blank_sword_1"), -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "blank_sword_2"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.REPAIR_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "blank_sword_3"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}