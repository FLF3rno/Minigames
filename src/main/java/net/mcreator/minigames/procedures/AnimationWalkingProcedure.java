package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class AnimationWalkingProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity.getDeltaMovement().x() < -0.01 || entity.getDeltaMovement().z() < -0.01 || entity.getDeltaMovement().x() > 0.01 || entity.getDeltaMovement().z() > 0.01;
	}
}