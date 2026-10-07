package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;

public class ReturnAllPotionEffectAmplifierProcedure {
	public static ArrayList<Double> execute(Entity entity) {
		if (entity == null)
			return new ArrayList<>();
		ArrayList<Double> list = new ArrayList<>();
		for (MobEffectInstance effect : ReturnAllPotionEffectsProcedure.getSortedActiveEffects(entity)) {
			list.add((double) effect.getAmplifier());
		}
		return list;
	}
}