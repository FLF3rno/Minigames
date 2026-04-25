package net.mcreator.minigames.item;

import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.BlankLongswordInHandProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class BlankLongSwordItem extends Item {
	public BlankLongSwordItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant().attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_0"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_1"), -3.15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_2"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(MinigamesModAttributes.SHOW_FULL_HEALTH_MOBS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_3"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(MinigamesModAttributes.STUN_FULL_HEALTH_MOBS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_4"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(MinigamesModAttributes.REPAIR_VALUE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blank_long_sword_5"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.blank_long_sword.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.blank_long_sword.description_1"));
		componentConsumer.accept(Component.translatable("item.minigames.blank_long_sword.description_2"));
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
		super.inventoryTick(itemstack, world, entity, equipmentSlot);
		if (equipmentSlot == EquipmentSlot.MAINHAND)
			BlankLongswordInHandProcedure.execute(itemstack);
	}
}