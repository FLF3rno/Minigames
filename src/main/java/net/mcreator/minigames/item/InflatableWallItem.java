package net.mcreator.minigames.item;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;

import net.mcreator.minigames.procedures.InflatableWallAddedProcedure;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Consumer;

public class InflatableWallItem extends Item {
	public InflatableWallItem(Item.Properties properties) {
		super(properties.stacksTo(1).attributes(ItemAttributeModifiers.builder()
				.add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "inflatable_wall_0"), 1000, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.inflatable_wall.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.inflatable_wall.description_1"));
		componentConsumer.accept(Component.translatable("item.minigames.inflatable_wall.description_2"));
		componentConsumer.accept(Component.translatable("item.minigames.inflatable_wall.description_3"));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		InflatableWallAddedProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getClickedFace(), context.getPlayer(),
				context.getItemInHand());
		return InteractionResult.SUCCESS;
	}
}
