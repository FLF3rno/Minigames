package net.mcreator.minigames.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.GoldenMangoUsedProcedure;
import net.mcreator.minigames.procedures.GoldenMangoDescriptionProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Consumer;

public class GoldenMangoItem extends Item {
	public GoldenMangoItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant().food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0f).alwaysEdible().build(), Consumables.defaultFood().consumeSeconds(1F).build())
				.attributes(ItemAttributeModifiers.builder()
						.add(MinigamesModAttributes.ABILITY_COOLDOWN, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_0"), 400, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_1"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_LENGTH, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_2"), 80, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_LENGTH_2, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_3"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_POTENCY, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_4"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_POTENCY_2, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_5"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.ABILITY_RANGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_6"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EXTRA_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "golden_mango_7"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		Entity entity = MinigamesMod.clientPlayer();
		String hoverText = GoldenMangoDescriptionProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				componentConsumer.accept(Component.literal(line));
			}
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		GoldenMangoUsedProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
		return itemstack;
	}
	

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}