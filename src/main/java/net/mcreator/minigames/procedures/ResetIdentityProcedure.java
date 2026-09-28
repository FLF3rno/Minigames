package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class ResetIdentityProcedure {
	public static void execute(LevelAccessor world, Entity entity, boolean resetInventory) {
		ResetIdentitiesProcedure.execute(world, entity, resetInventory);
	}
}
