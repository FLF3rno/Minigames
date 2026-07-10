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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.ComboDaggerHitProcedure;
import net.mcreator.minigames.procedures.ComboDaggerDescriptionProcedure;
import net.mcreator.minigames.procedures.BreakWeaponProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class ComboDaggerItem extends Item {
	public ComboDaggerItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant()
				.attributes(ItemAttributeModifiers.builder()
						.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "combo_dagger_0"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "combo_dagger_1"), -1.6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "combo_dagger_2"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.REPAIR_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "combo_dagger_3"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		Entity entity = MinigamesMod.clientPlayer();
		String hoverText = ComboDaggerDescriptionProcedure.execute();
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				componentConsumer.accept(Component.literal(line));
			}
		}
	}

	@Override
	public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		super.hurtEnemy(itemstack, entity, sourceentity);
		ComboDaggerHitProcedure.execute(sourceentity);
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
		super.inventoryTick(itemstack, world, entity, equipmentSlot);
		BreakWeaponProcedure.execute();
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}