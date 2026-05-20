package net.mcreator.minigames;

import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.TriState;

import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber
public class DungeonItemPickupBlocker {
	@SubscribeEvent
	public static void blockPickup(ItemEntityPickupEvent.Pre event) {
		if (isBlockedDungeonItem(event.getItemEntity())) {
			event.setCanPickup(TriState.FALSE);
		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof ItemEntity itemEntity) {
			if (DungeonItemAccess.isDungeonItem(itemEntity.getItem())) {
				itemEntity.lifespan = Integer.MAX_VALUE;
			}
		}
	}

	@SubscribeEvent
	public static void onItemToss(ItemTossEvent event) {
		if (event.getPlayer() == null)
			return;
		if (!MinigamesModVariables.MapVariables.get(event.getPlayer().level()).inCombat)
			return;
		if (DungeonItemAccess.isDungeonItem(event.getEntity().getItem())) {
			event.getPlayer().getInventory().placeItemBackInInventory(event.getEntity().getItem().copy());
			event.setCanceled(true);
		}
	}

	private static boolean isBlockedDungeonItem(ItemEntity itemEntity) {
		return DungeonItemAccess.isDungeonItem(itemEntity.getItem());
	}
}
