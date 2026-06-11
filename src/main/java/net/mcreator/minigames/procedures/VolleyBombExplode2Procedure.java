package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class VolleyBombExplode2Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		ExplodeProcedure.execute(world, x, y, z, entity, false, true, entity.getPersistentData().getDoubleOr("explosionDamage", 0), Math.sqrt(entity.getPersistentData().getDoubleOr("explosionDamage", 0)) * 0.5,
				entity.getPersistentData().getDoubleOr("explosionSize", 0), "normal");
		if (!entity.level().isClientSide())
			entity.discard();
	}
}