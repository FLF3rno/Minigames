package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.FlavioFightManager;

import java.util.Comparator;

public class BlessingDispenserEntityDiesProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		Entity laser = null;
		if (!world.isClientSide()) {
			net.mcreator.minigames.FlavioFightManager.dispensersAlive--;
		}
		if (net.mcreator.minigames.FlavioFightManager.dispensersAlive <= 0) {
			if (findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 60) != null) {
				laser = findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 60);
				ExplodeProcedure.execute(world, laser.getX(), laser.getY(), laser.getZ(), laser, true, true, 0, 1, 3, "normal");
				if (!world.isClientSide()) {
					net.mcreator.minigames.FlavioFightManager.nextPhase(world);
				}
				if (!laser.level().isClientSide())
					laser.discard();
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}