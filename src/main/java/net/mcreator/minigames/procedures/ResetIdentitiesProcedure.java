package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.AccountSwapManager;

public class ResetIdentitiesProcedure {
	public static void execute(LevelAccessor world, Entity entity, boolean resetInventory) {
		if (world == null || world.isClientSide()) {
			return;
		}
		AccountSwapManager.resetIdentity(world, entity, resetInventory);
	}
}