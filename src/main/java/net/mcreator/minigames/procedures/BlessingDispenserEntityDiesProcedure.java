package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.entity.BlessingDispenserEntity;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.FlavioFightManager;

import java.util.Comparator;

public class BlessingDispenserEntityDiesProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MinigamesMod.queueServerWork(30, () -> {
			if (findEntityInWorldRange(world, BlessingDispenserEntity.class, x, y, z, 30) == null) {
				if ((findEntityInWorldRange(world, FlavioEntity.class, x, y, z, 30)) instanceof LivingEntity _entity)
					_entity.removeEffect(MinigamesModMobEffects.BLESSED);
				ExplodeProcedure.execute(world, (findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 30)).getX(), (findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 30)).getY(),
						(findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 30)).getZ(), findEntityInWorldRange(world, Player.class, x, y, z, 20), true, true, 0, 1, 3, "normal");
				net.mcreator.minigames.FlavioFightManager.nextPhase(world);
				if (!(findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 30)).level().isClientSide())
					(findEntityInWorldRange(world, FlavioOmegaLaserEntity.class, x, y, z, 30)).discard();
			}
		});
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}