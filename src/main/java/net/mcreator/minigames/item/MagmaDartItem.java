package net.mcreator.minigames.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.procedures.MagmaDartRightclickedProcedure;
import net.mcreator.minigames.procedures.MagmaDartPropertyValueProviderProcedure;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import com.mojang.serialization.MapCodec;

public class MagmaDartItem extends Item {
	public MagmaDartItem(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, tooltipDisplay, componentConsumer, flag);
		componentConsumer.accept(Component.translatable("item.minigames.magma_dart.description_0"));
		componentConsumer.accept(Component.translatable("item.minigames.magma_dart.description_1"));
	}

	@Override
	public InteractionResult use(Level world, Player entity, InteractionHand hand) {
		ItemStack itemstack = entity.getItemInHand(hand);
		MagmaDartRightclickedProcedure.execute(entity, itemstack);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, ServerLevel world, net.minecraft.world.entity.Entity entity, EquipmentSlot slot) {
		super.inventoryTick(itemstack, world, entity, slot);
		boolean selected = entity instanceof LivingEntity living && living.getItemBySlot(slot) == itemstack;
		if (!selected) {
			itemstack.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
			return;
		}
		if (MagmaDartPropertyValueProviderProcedure.execute(entity) == 1) {
			itemstack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.literal("Magma Shockwave").withStyle(style -> style.withItalic(false)));
		} else {
			itemstack.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
		}
	}

	public record ShockwaveProperty() implements RangeSelectItemModelProperty {
		public static final MapCodec<ShockwaveProperty> MAP_CODEC = MapCodec.unit(new ShockwaveProperty());

		@Override
		public float get(ItemStack itemStackToRender, @Nullable ClientLevel clientWorld, @Nullable LivingEntity entity, int seed) {
			return (float) MagmaDartPropertyValueProviderProcedure.execute(entity);
		}

		@Override
		public MapCodec<ShockwaveProperty> type() {
			return MAP_CODEC;
		}
	}
}
