package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class WindScytheInHandProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("previousX", (entity.getPersistentData().getDoubleOr("currentX", 0)));
		entity.getPersistentData().putDouble("previousZ", (entity.getPersistentData().getDoubleOr("currentZ", 0)));
		entity.getPersistentData().putDouble("currentX", (entity.getX()));
		entity.getPersistentData().putDouble("currentZ", (entity.getZ()));
	}
}