package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
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
import net.minecraft.world.phys.HitResult;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig;
import net.mcreator.minigames.procedures.BoxingMasteryHandler;

@EventBusSubscriber(value = Dist.CLIENT)
public class DungeonWeaponClickBlocker {
	public static boolean suppressNextSwing = false;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		suppressNextSwing = false;
	}

	@SubscribeEvent
	public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
		if (!event.isAttack())
			return;

		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player == null)
			return;

		ItemStack mainHand = player.getMainHandItem();
		boolean playingDungeons = player.level() != null
				&& MinigamesModVariables.MapVariables.get(player.level()).playingDungeons;

		boolean isBoxing = BoxingMasteryHandler.isBoxing(player);

		if (playingDungeons && !DungeonItemAccess.isDungeonWeapon(mainHand) && !isBoxing) {
			event.setCanceled(true);
			event.setSwingHand(false);
			suppressNextSwing = true;
			return;
		}

		boolean onCooldown = player.getAttackStrengthScale(0f) < 1.0f
				|| player.getCooldowns().isOnCooldown(mainHand)
				|| (getAttackCooldownAttribute(mainHand) > 0 && player.getCooldowns().isOnCooldown(mainHand));

		if (onCooldown) {
			event.setCanceled(true);
			boolean suppressSwing = !playingDungeons || MinigamesSettingsConfig.getBoolean("roguelikeSuppressSwingNotReady", true);
			if (suppressSwing) {
				event.setSwingHand(false);
				suppressNextSwing = true;
			}
			return;
		}

		if (isBoxing) {
			event.setSwingHand(false);
			suppressNextSwing = true;
			BoxingMasteryHandler.triggerPunch(player);
			if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
				player.resetAttackStrengthTicker();
			}
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