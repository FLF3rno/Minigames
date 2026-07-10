package net.mcreator.minigames.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.SymmetricalShovelItemInInventoryTickProcedure;

import javax.annotation.Nullable;

import java.util.function.Consumer;

public class SymmetricalShovelItem extends Item {
	public SymmetricalShovelItem(Item.Properties properties) {
		super(properties.durability(14));
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		return 20000f;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.symmetrical_shovel.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.symmetrical_shovel.description_1"));
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
		super.inventoryTick(itemstack, world, entity, equipmentSlot);
		SymmetricalShovelItemInInventoryTickProcedure.execute(world, entity, itemstack);
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}