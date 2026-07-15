package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClearEnderchestProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player player)
			for (int i = 0; i < player.getEnderChestInventory().getContainerSize(); i++) {
    		player.getEnderChestInventory().setItem(i, ItemStack.EMPTY);
		}
	}
}