package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.mcreator.minigames.DungeonItemAccess;

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
		}
	}
}
