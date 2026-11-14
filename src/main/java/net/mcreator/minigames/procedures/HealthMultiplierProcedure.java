package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;

public class HealthMultiplierProcedure {

	public static void execute(LevelAccessor world, Entity entity) {
		apply(world, entity);
	}

	private static void apply(LevelAccessor world, Entity entity) {
		if (world == null || entity == null) {
			return;
		}
		if (entity instanceof LivingEntity le) {
			var maxHpAttr = le.getAttribute(Attributes.MAX_HEALTH);
			if (maxHpAttr != null && entity.getPersistentData().getBooleanOr("modifiedHP", false)) {
				double scaled = le.getMaxHealth() * (1 + (world.players().size() - 1) * 0.75);
				maxHpAttr.setBaseValue(scaled * 1f);
				le.setHealth((float) scaled);
				if (!world.isClientSide())
					entity.getPersistentData().putBoolean("modifiedHP", true);
			}
		}
	}
}
