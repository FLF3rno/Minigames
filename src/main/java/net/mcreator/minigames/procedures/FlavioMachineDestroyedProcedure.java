package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.FlavioFightManager;

import java.util.Comparator;

public class FlavioMachineDestroyedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ExplodeProcedure.execute(world, x, y, z, findEntityInWorldRange(world, Player.class, x, y, z, 20), true, true, 0, 2.5, 3, "normal");
		if (net.mcreator.minigames.FlavioFightManager.phase == 3) {
			net.mcreator.minigames.FlavioFightManager.phase += 0.5;
		} else {
			net.mcreator.minigames.FlavioFightManager.nextPhase(world);
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}