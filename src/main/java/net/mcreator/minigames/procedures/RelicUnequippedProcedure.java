package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;

public class RelicUnequippedProcedure {
	public static void execute() {
		execute(null, ItemStack.EMPTY);
	}

	public static void execute(net.minecraft.world.entity.Entity entity, ItemStack item) {
		if (entity == null)
			return;
	}
}