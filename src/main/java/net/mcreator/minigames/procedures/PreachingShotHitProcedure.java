package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

public class PreachingShotHitProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		double force = 0;
		force = -1.75;
		entity.setDeltaMovement(new Vec3((immediatesourceentity.getLookAngle().x * force), (immediatesourceentity.getLookAngle().y * force), (immediatesourceentity.getLookAngle().z * force)));
		sourceentity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.MOB_PROJECTILE)), 4);
	}
}