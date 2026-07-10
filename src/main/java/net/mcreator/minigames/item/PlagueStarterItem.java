package net.mcreator.minigames.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.procedures.PlagueStarterRightclickedProcedure;
import net.mcreator.minigames.procedures.PlagueStarterDescriptionProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Consumer;

public class PlagueStarterItem extends Item {
	public PlagueStarterItem(Item.Properties properties) {
		super(properties.stacksTo(1).fireResistant()
				.attributes(ItemAttributeModifiers.builder()
						.add(MinigamesModAttributes.SALVAGE_VALUE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "plague_starter_0"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EXTRA_DAMAGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "plague_starter_1"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.ABILITY_COOLDOWN, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "plague_starter_2"), 800, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.EFFECT_LENGTH, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "plague_starter_3"), 200, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(MinigamesModAttributes.ABILITY_RANGE, new AttributeModifier(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "plague_starter_4"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		Entity entity = MinigamesMod.clientPlayer();
		String hoverText = PlagueStarterDescriptionProcedure.execute(itemstack);
		if (hoverText != null) {
			for (String line : hoverText.split("\n")) {
				componentConsumer.accept(Component.literal(line));
			}
		}
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		InteractionResult ar = super.use(world, entity, hand);
		PlagueStarterRightclickedProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, entity.getItemInHand(hand));
		return ar;
	}

	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged && !oldStack.equals(newStack);
	}
}