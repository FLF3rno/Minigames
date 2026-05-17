package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.mcreator.minigames.DungeonItemAccess;

@EventBusSubscriber
public class DungeonWeaponUsageRulesProcedure {
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getHand() == InteractionHand.OFF_HAND && DungeonItemAccess.isDungeonItem(event.getItemStack())) {
			event.setCancellationResult(InteractionResult.FAIL);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		if (!(event.getEntity() instanceof Player player))
			return;
		if (player.level().isClientSide())
			return;
		ItemStack offhand = player.getOffhandItem();
		if (DungeonItemAccess.isDungeonItem(offhand)) {
			if (!player.addItem(offhand.copy())) {
				player.drop(offhand.copy(), false);
			}
			player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
		}
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player))
			return;
		ItemStack mainHand = player.getMainHandItem();
		if (!DungeonItemAccess.isDungeonItem(mainHand))
			return;
		if (player.getAttackStrengthScale(0f) < 0.999f) {
			event.setCanceled(true);
		}
	}

}
