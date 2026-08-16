package net.mcreator.minigames.item;

import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.AttackSpeedGemTickProcedure;
import net.mcreator.minigames.procedures.AttackSpeedGemDroppedProcedure;
import net.mcreator.minigames.procedures.AttackSpeedGemDescriptionProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class AttackSpeedGemItem extends Item {
	public AttackSpeedGemItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant()
				.attributes(ItemAttributeModifiers.builder()
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "attack_speed_gem_0"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_POTENCY, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "attack_speed_gem_1"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		Entity entity = MinigamesMod.clientPlayer();
		String hoverText = AttackSpeedGemDescriptionProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				componentConsumer.accept(Component.literal(line));
			}
		}
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
		super.inventoryTick(itemstack, world, entity, equipmentSlot);
		AttackSpeedGemTickProcedure.execute(entity, itemstack);
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, Player entity) {
		AttackSpeedGemDroppedProcedure.execute(entity);
		return true;
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}