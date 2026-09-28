package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.mixin.CooldownInstanceAccessor;
import net.mcreator.minigames.mixin.ItemCooldownsAccessor;
import net.mcreator.minigames.mixin.LivingEntityAccessor;
import net.mcreator.minigames.network.SyncAttackStrengthMessage;

import java.util.Map;

public class ModifyPercentageCooldownProcedure {
	public static void execute(LevelAccessor world, Entity target, ItemStack item, boolean affectAttackSpeedCooldown, boolean affectItemCooldown, double percentage) {
		if (!(target instanceof Player player))
			return;

		double factor;
		if (percentage <= 0.0) {
			if (percentage == 0.0) {
				factor = 1.0;
			} else if (percentage >= -1.0) {
				factor = 1.0 - percentage;
			} else {
				factor = 1.0 - (percentage / 100.0);
			}
		} else if (percentage <= 1.0) {
			factor = percentage;
		} else if (percentage <= 100.0) {
			factor = 1.0 - (percentage / 100.0);
		} else {
			factor = percentage / 100.0;
		}

		ItemStack targetItem = (item == null || item.isEmpty()) ? player.getMainHandItem() : item;

		if (affectAttackSpeedCooldown) {
			double attackSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
			float delay = attackSpeed > 0.0 ? (float) (20.0D / attackSpeed) : 20.0F;
			if (delay > 0.0F) {
				int currentTicker = ((LivingEntityAccessor) player).getAttackStrengthTicker();
				double remainingAttackTicks = Math.max(0.0, (double) delay - (double) currentTicker);
				if (remainingAttackTicks > 0.0) {
					double newRemaining = remainingAttackTicks * factor;
					int newTicker;
					if (newRemaining <= 0.0) {
						newTicker = (int) Math.ceil(delay);
					} else {
						newTicker = Math.max(0, (int) Math.round((double) delay - newRemaining));
					}
					((LivingEntityAccessor) player).setAttackStrengthTicker(newTicker);
					if (player instanceof ServerPlayer serverPlayer) {
						PacketDistributor.sendToPlayer(serverPlayer, new SyncAttackStrengthMessage(newTicker));
					}
				}
			}

			if (!affectItemCooldown && !targetItem.isEmpty() && hasAttackCooldownAttribute(targetItem)) {
				modifyItemCooldown(player, targetItem, factor);
			}
		}

		if (affectItemCooldown && !targetItem.isEmpty()) {
			modifyItemCooldown(player, targetItem, factor);
		}
	}

	private static void modifyItemCooldown(Player player, ItemStack stack, double factor) {
		ItemCooldowns cooldowns = player.getCooldowns();
		if (!(cooldowns instanceof ItemCooldownsAccessor accessor))
			return;

		Identifier group = accessor.callGetCooldownGroup(stack);
		Map<Identifier, ?> map = accessor.getCooldowns();
		Object instanceObj = map.get(group);
		if (!(instanceObj instanceof CooldownInstanceAccessor instance))
			return;

		int currentTick = accessor.getTickCount();
		int remainingTicks = instance.getEndTime() - currentTick;
		if (remainingTicks <= 0)
			return;

		int newRemainingTicks = (int) Math.round(remainingTicks * factor);
		if (newRemainingTicks == remainingTicks)
			return;

		if (newRemainingTicks <= 0) {
			cooldowns.removeCooldown(group);
		} else {
			cooldowns.addCooldown(stack, newRemainingTicks);
		}
	}

	private static boolean hasAttackCooldownAttribute(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot() == EquipmentSlotGroup.MAINHAND && entry.attribute().is(MinigamesModAttributes.ATTACK_COOLDOWN)) {
				if (entry.modifier().amount() > 0.0) {
					return true;
				}
			}
		}
		return false;
	}
}