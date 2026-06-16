package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.minigames.client.LivingEntityTransparencyDataAccessor;

public class PhantomEffectEndsProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("transparency", 0);
		if (entity instanceof LivingEntity livingEntity && livingEntity instanceof LivingEntityTransparencyDataAccessor transparencyData) {
			transparencyData.minigames$setTransparency(0.0f);
		}
	}
}
