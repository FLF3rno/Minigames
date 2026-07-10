package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModAttributes;

@EventBusSubscriber(value = Dist.CLIENT)
public class DungeonWeaponClickBlocker {
	@SubscribeEvent
	public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
		if (!event.isAttack())
			return;

		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null)
			return;

		ItemStack mainHand = player.getMainHandItem();
		if (!DungeonItemAccess.isDungeonItem(mainHand))
			return;

		if (player.getAttackStrengthScale(0f) < 1.0f) {
			event.setCanceled(true);
			return;
		}

		if (getAttackCooldownAttribute(mainHand) > 0
				&& player.getCooldowns().isOnCooldown(mainHand)) {
			event.setCanceled(true);
		}
	}

	private static double getAttackCooldownAttribute(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(
				DataComponents.ATTRIBUTE_MODIFIERS,
				ItemAttributeModifiers.EMPTY
		);

		double total = 0.0;

		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot() != EquipmentSlotGroup.MAINHAND)
				continue;

			if (entry.attribute().is(MinigamesModAttributes.ATTACK_COOLDOWN)) {
				AttributeModifier modifier = entry.modifier();
				total += modifier.amount();
			}
		}

		return total;
	}
}