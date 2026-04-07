package net.mcreator.minigames.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.GrapplingHookRightclickedProcedure;

import java.util.function.Consumer;

public class GrapplingHookItem extends Item {
	public GrapplingHookItem(Item.Properties properties) {
		super(properties.durability(2));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.grappling_hook.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.grappling_hook.description_1"));
		componentConsumer.accept(Component.translatable("item.minigames.grappling_hook.description_2"));
		componentConsumer.accept(Component.translatable("item.minigames.grappling_hook.description_3"));
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = super.use(world, entity, hand);
		GrapplingHookRightclickedProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, entity.getItemInHand(hand));
		return ar;
	}
}