package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

public class GrantGameCompassProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player) {
			ItemStack _setstack = new ItemStack(MinigamesModItems.GAME_COMPASS.get()).copy();
			_setstack.setCount(1);
			_player.getInventory().placeItemBackInInventory(_setstack);
		}
	}
}