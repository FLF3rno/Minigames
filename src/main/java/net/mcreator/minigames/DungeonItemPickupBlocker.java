package net.mcreator.minigames;

import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.TriState;

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

	private static boolean isBlockedDungeonItem(ItemEntity itemEntity) {
		return DungeonItemAccess.isDungeonItem(itemEntity.getItem());
	}
}
