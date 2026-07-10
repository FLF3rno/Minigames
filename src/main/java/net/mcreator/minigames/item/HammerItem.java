package net.mcreator.minigames.item;

import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.HammerHitProcedure;
import net.mcreator.minigames.procedures.HammerDescriptionProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Consumer;

public class HammerItem extends Item {
	public HammerItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant()
				.attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hammer_0"), 12, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hammer_1"), -2.8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hammer_2"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.REPAIR_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hammer_3"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_LENGTH, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "hammer_4"), 200, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		Entity entity = MinigamesMod.clientPlayer();
		String hoverText = HammerDescriptionProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				componentConsumer.accept(Component.literal(line));
			}
		}
	}

	@Override
	public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		super.hurtEnemy(itemstack, entity, sourceentity);
		HammerHitProcedure.execute(entity, itemstack);
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}