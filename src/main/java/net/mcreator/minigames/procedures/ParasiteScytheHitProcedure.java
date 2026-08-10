package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class ParasiteScytheHitProcedure {
	public static void execute(Entity sourceentity) {
		if (sourceentity == null)
			return;
		if (!sourceentity.getPersistentData().getBooleanOr("parasiteScytheActive", false)) {
			sourceentity.getPersistentData().putBoolean("parasiteScytheActive", true);
		}
	}
}