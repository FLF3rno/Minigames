package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.minigames.client.LivingEntityTransparencyDataAccessor;

public class PhantomEffectAppliedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("transparency", 50);
		if (entity instanceof LivingEntity livingEntity && livingEntity instanceof LivingEntityTransparencyDataAccessor transparencyData) {
			transparencyData.minigames$setTransparency(50.0f);
		}
	}
}
