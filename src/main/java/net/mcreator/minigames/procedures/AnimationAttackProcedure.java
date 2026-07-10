package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class AnimationAttackProcedure {
	private static final String LOCAL_ATTACK_COOLDOWN_KEY = "local_attack_cooldown";

	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof LivingEntity _livEnt0 && _livEnt0.swinging && entity.getPersistentData().getInt(LOCAL_ATTACK_COOLDOWN_KEY, 0) <= 0;
	}
}
