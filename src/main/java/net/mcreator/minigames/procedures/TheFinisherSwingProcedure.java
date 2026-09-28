package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class TheFinisherSwingProcedure {
	public static void execute(Entity entity, Entity sourceentity, ItemStack itemstack) {
		if (entity == null || sourceentity == null)
			return;
		if (!(sourceentity instanceof Player _plrCldCheck1 && _plrCldCheck1.getCooldowns().isOnCooldown(itemstack))) {
			if (entity.isAlive()) {
				ApplyCooldownProcedure.execute(sourceentity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:attack_cooldown"));
			}
		}
	}
}