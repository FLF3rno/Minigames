package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class ApplyCooldownProcedure {
	public static void execute(Entity target, ItemStack item, double ticks) {
		if (target == null)
			return;
		if (target instanceof Player _player)
			_player.getCooldowns().addCooldown(item, (int) ticks);
	}
}