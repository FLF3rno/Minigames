package net.mcreator.minigames.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.mcreator.minigames.init.MinigamesModAttributes;

public class ItemSizeHelper {

    public static float getItemScale(ItemStack stack, EquipmentSlot slot) {
        if (stack == null || stack.isEmpty()) {
            return 1.0f;
        }

        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null) {
            modifiers = stack.getItem().components().get(DataComponents.ATTRIBUTE_MODIFIERS);
        }

        if (modifiers != null) {
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                if (entry.slot().test(slot) && entry.attribute().equals(MinigamesModAttributes.ITEM_SIZE)) {
                    AttributeModifier modifier = entry.modifier();
                    double amount = modifier.amount();
                    if (amount > 0) {
                        return (float) amount;
                    }
                }
            }
        }

        return 1.0f;
    }

    public static float getItemScale(ItemStack stack, InteractionHand hand) {
        return getItemScale(stack, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }
}
