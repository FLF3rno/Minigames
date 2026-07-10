package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public class GetItemAttributeProcedure {

    public static double execute(ItemStack item, String attribute) {
        if (item == null || item.isEmpty() || attribute == null || attribute.isEmpty())
            return 0.0;

        Optional<Holder.Reference<Attribute>> attributeHolder =
                BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(attribute));

        if (attributeHolder.isEmpty())
            return 0.0;

        Holder<Attribute> attributeTarget = attributeHolder.get();

        ItemAttributeModifiers modifiers =
                item.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        double total = 0.0;

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().equals(attributeTarget)) {
                total += entry.modifier().amount();
            }
        }

        CustomData customData =
                item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        CompoundTag tag = customData.copyTag();

        int forgedAmount = tag.getInt("forged").orElse(0);
        int glitchedAmount = tag.getInt("glitched").orElse(0);

        if (forgedAmount != 0) {
            total += total * (forgedAmount / 100.0);
        }

        if (glitchedAmount != 0) {
            if (glitchedAmount > 0) {
                total += total * (glitchedAmount / 100.0);
            } else {
                double divisor = 1.0 + (Math.abs(glitchedAmount) / 100.0);
                total /= divisor;
            }
        }

        return total;
    }
}