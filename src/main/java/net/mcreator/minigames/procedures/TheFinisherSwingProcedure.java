package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class TheFinisherSwingProcedure {
	public static void execute(Entity sourceentity, ItemStack itemstack) {
		if (sourceentity == null)
			return;
		if (!(sourceentity instanceof Player _plrCldCheck1 && _plrCldCheck1.getCooldowns().isOnCooldown(itemstack))) {
			ApplyCooldownProcedure.execute(sourceentity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:attack_cooldown"));
		}
	}
}