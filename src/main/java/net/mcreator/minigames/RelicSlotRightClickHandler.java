package net.mcreator.minigames;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.InteractionResult;

@EventBusSubscriber
public class RelicSlotRightClickHandler {
	private static final int LEFT_RELIC_SLOT = 34;
	private static final int RIGHT_RELIC_SLOT = 35;

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		ItemStack heldStack = event.getItemStack();
		if (heldStack.isEmpty() || !DungeonItemAccess.isRelic(heldStack)) {
			return;
		}

		if (player.level().isClientSide()) {
			event.setCancellationResult(InteractionResult.SUCCESS);
			event.setCanceled(true);
			return;
		}

		Inventory inventory = player.getInventory();
		int targetSlot = inventory.getItem(LEFT_RELIC_SLOT).isEmpty() ? LEFT_RELIC_SLOT : inventory.getItem(RIGHT_RELIC_SLOT).isEmpty() ? RIGHT_RELIC_SLOT : -1;
		if (targetSlot == -1) {
			event.setCancellationResult(InteractionResult.FAIL);
			event.setCanceled(true);
			return;
		}

		inventory.setItem(targetSlot, heldStack.copy());
		heldStack.setCount(0);
		player.containerMenu.broadcastChanges();
		event.setCancellationResult(InteractionResult.SUCCESS);
		event.setCanceled(true);
	}
}
