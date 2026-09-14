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
			
			boolean allDead = net.mcreator.minigames.FlavioFightManager.dispensersAlive <= 0;
			if (!allDead) {
				long aliveDispensers = world.getEntitiesOfClass(net.mcreator.minigames.entity.BlessingDispenserEntity.class, 
						new AABB(new Vec3(x, y, z), new Vec3(x, y, z)).inflate(250), 
						Entity::isAlive).size();
				if (aliveDispensers <= 0) {
					allDead = true;
				}
			}

			if (allDead) {
				net.mcreator.minigames.FlavioFightManager.nextPhase(world);

				Entity laserEntity = findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 250);
				if (laserEntity != null) {
					ExplodeProcedure.execute(world, laserEntity.getX(), laserEntity.getY(), laserEntity.getZ(), laserEntity, true, true, 0, 1, 3, "normal");
					laserEntity.discard();
				}
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), Entity::isAlive).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}