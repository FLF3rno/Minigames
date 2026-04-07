package net.mcreator.minigames.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class SnowShovelItem extends Item {
	public SnowShovelItem(Item.Properties properties) {
		super(properties.durability(4));
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		return 20000f;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.snow_shovel.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.snow_shovel.description_1"));
	}
}