package net.mcreator.minigames.item.inventory;

import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.world.inventory.StartGameMenu;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nonnull;

@EventBusSubscriber
public class DungeonCompassInventoryCapability extends ComponentItemHandler {
	@SubscribeEvent
	public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == MinigamesModItems.DUNGEON_COMPASS.get()) {
			Player player = event.getPlayer();
			if (player.containerMenu instanceof StartGameMenu)
				player.closeContainer();
		}
	}

	public DungeonCompassInventoryCapability(MutableDataComponentHolder parent) {
		super(parent, DataComponents.CONTAINER, 9);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() != MinigamesModItems.DUNGEON_COMPASS.get();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot).copy();
	}
}