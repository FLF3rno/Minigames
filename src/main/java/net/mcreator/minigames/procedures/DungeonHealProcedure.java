package net.mcreator.minigames.procedures;

import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModMobEffects;

public class DungeonHealProcedure {
	public static void execute(Entity target, double amount, String type) {
		if (target == null || type == null)
			return;
		String Type = "";
		double Amount = 0;
		Entity Target = null;
		Target = target;
		Amount = amount;
		Type = type;
		if (Target instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.BLOCK_HEAL)) {
			Amount = Amount - Amount * (((target instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.BLOCK_HEAL) ? _livEnt.getEffect(MinigamesModMobEffects.BLOCK_HEAL).getAmplifier() : 0) + 1) / 100d);
		}
		if (Target instanceof LivingEntity _entity)
			_entity.setHealth((float) ((target instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + Amount));
	}
}