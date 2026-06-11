package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;

import java.util.Optional;

public class GetItemAttributeProcedure {

    public static double execute(ItemStack item, String attribute) {
        if (item == null || attribute == null || attribute.isEmpty())
            return 0.0;

        Optional<Holder.Reference<Attribute>> attributeHolder =
                BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(attribute));

        if (attributeHolder.isEmpty())
            return 0.0;

        Holder<Attribute> attributeTarget = attributeHolder.get();

        ItemAttributeModifiers modifiers = item.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, 
                                            item.getItem().getDefaultAttributeModifiers(item));
        
        double total = 0.0;

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().equals(attributeTarget)) {
                total += entry.modifier().amount();
            }
        }

        return total;
    }
}