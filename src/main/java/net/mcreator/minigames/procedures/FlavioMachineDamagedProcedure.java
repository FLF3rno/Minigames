package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.FlavioFightManager;

import java.util.Comparator;

public class FlavioMachineDamagedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double amount) {
		if (entity == null)
			return;
		double mult = 0;
		Entity flavio = null;
		flavio = findEntityInWorldRange(world, FlavioEntity.class, x, y, z, 50);
		if (net.mcreator.minigames.FlavioFightManager.phase == 1) {
			mult = (double) (flavio instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
			mult = mult / 4;
			if (flavio instanceof LivingEntity _entity)
				_entity.setHealth((float) ((flavio instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - mult * amount));
		}
		if (net.mcreator.minigames.FlavioFightManager.phase == 3) {
			mult = (double) (flavio instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
			mult = mult / 8;
			if (flavio instanceof LivingEntity _entity)
				_entity.setHealth((float) ((flavio instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) - mult * amount));
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}