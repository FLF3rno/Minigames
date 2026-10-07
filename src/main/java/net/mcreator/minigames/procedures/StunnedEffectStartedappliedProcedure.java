package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.StunnedEffectEntity;

public class StunnedEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		if (entity instanceof net.minecraft.world.entity.Mob mob) {
			mob.setNoAi(true);
		}

		if (!(world instanceof ServerLevel level))
			return;

		boolean alreadyHasOverlay = level.getEntitiesOfClass(
	StunnedEffectEntity.class,
	new AABB(
		entity.getX() - 64,
		entity.getY() - 64,
		entity.getZ() - 64,
		entity.getX() + 64,
		entity.getY() + 64,
		entity.getZ() + 64
	)
).stream().anyMatch(e -> e.getParentId() == entity.getId());

if (alreadyHasOverlay)
	return;
		Entity entityToSpawn = MinigamesModEntities.STUNNED_EFFECT.get().spawn(
			level,
			BlockPos.containing(x, y + entity.getBbHeight() + 0.5, z),
			EntitySpawnReason.MOB_SUMMONED
		);

		if (entityToSpawn instanceof StunnedEffectEntity stunnedEffect) {
			stunnedEffect.setDeltaMovement(0, 0, 0);
			stunnedEffect.setParent(entity);
		}
	}
}



