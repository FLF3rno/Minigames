package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class HumanCannonballInventoryCooldownProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBooleanOr("humanCannonball", false)) {
			if (entity instanceof Player _player)
				_player.getCooldowns().addCooldown(itemstack, (int) GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		}
	}
}