package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class PotheistHitProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		double num = 0;
		num = 0;
		if (!ReturnAllPotionEffectsProcedure.execute(entity).isEmpty()) {
			for (int _i1 = 0; _i1 < (int) ReturnAllPotionEffectsProcedure.execute(entity).size(); _i1++) {
				ApplyEffectProcedure.execute(world, sourceentity, false, ReturnAllPotionEffectAmplifierProcedure.execute(entity).get((int) num) instanceof Double _doub2 ? _doub2 : 0.0D,
						ReturnAllPotionEffectDurationProcedure.execute(entity).get((int) num) instanceof Double _doub3 ? _doub3 : 0.0D, ReturnAllPotionEffectsProcedure.execute(entity).get((int) num) instanceof String _str4 ? _str4 : "");
				num = num + 1;
			}
			if (entity instanceof LivingEntity _entity)
				_entity.removeAllEffects();
		}
	}
}