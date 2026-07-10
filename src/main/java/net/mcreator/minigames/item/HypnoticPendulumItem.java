package net.mcreator.minigames.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.HypnoticPendulumRightclickedProcedure;

import java.util.function.Consumer;

public class HypnoticPendulumItem extends Item {
	public HypnoticPendulumItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
		return ItemUseAnimation.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
		return 20;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.hypnotic_pendulum.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.hypnotic_pendulum.description_1"));
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = super.use(world, entity, hand);
		entity.startUsingItem(hand);
		HypnoticPendulumRightclickedProcedure.execute(world, entity, entity.getItemInHand(hand));
		return ar;
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}