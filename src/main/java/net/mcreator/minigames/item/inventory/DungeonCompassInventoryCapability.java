package net.mcreator.minigames.item.inventory;

import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.world.inventory.StartGameMenu;
import net.mcreator.minigames.init.MinigamesModItems;

@EventBusSubscriber
public class DungeonCompassInventoryCapability extends ItemAccessItemHandler {
	@SubscribeEvent
	public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == MinigamesModItems.DUNGEON_COMPASS.get()) {
			Player player = event.getPlayer();
			if (player.containerMenu instanceof StartGameMenu)
				player.closeContainer();
		}
	}

	public DungeonCompassInventoryCapability(ItemAccess access) {
		super(access, DataComponents.CONTAINER, 9);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return super.isValid(index, resource) && resource.getItem() != MinigamesModItems.DUNGEON_COMPASS.get();
	}
}