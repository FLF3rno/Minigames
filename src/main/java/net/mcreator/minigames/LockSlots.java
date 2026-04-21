/*
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.minigames as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.minigames;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.lang.reflect.Field;

@EventBusSubscriber
public class LockSlots {
	private static Field selectedField;

	static {
		try {
			selectedField = Inventory.class.getDeclaredField("selected");
			selectedField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				selectedField = Inventory.class.getDeclaredField("selectedSlot");
				selectedField.setAccessible(true);
			} catch (NoSuchFieldException e2) {
			}
		}
	}

	public LockSlots() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new LockSlots();
	}

	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
	}

	private static int getSelectedSlot(Inventory inventory) {
		try {
			if (selectedField != null) {
				return selectedField.getInt(inventory);
			}
		} catch (IllegalAccessException e) {
		}
		return 0;
	}

	private static void setSelectedSlot(Inventory inventory, int slot) {
		try {
			if (selectedField != null) {
				selectedField.setInt(inventory, slot);
			}
		} catch (IllegalAccessException e) {
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		double playerSlots = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
		int slots = Math.max(0, Math.min(9, (int) playerSlots));
		Inventory inventory = player.getInventory();
		
		if (slots < 9 && slots > 0) {
			int currentSelected = getSelectedSlot(inventory);
			if (currentSelected >= slots) {
				// Wrapping logic: if we hit 8 (left scroll from 0), go to slots-1. Otherwise go to 0.
				if (currentSelected == 8) {
					setSelectedSlot(inventory, slots - 1);
				} else {
					setSelectedSlot(inventory, 0);
				}
			}
		}
		
		if (!player.level().isClientSide()) {
			for (int i = slots; i < 9; i++) {
				if (!inventory.getItem(i).isEmpty()) {
					player.drop(inventory.getItem(i), true);
					inventory.setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
				}
			}
		}
	}

	@EventBusSubscriber
	private static class LockSlotsForgeBusEvents {
		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {
		}
	}
}