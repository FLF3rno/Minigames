package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReturnAllPotionEffectsProcedure {
	public static List<MobEffectInstance> getSortedActiveEffects(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			List<MobEffectInstance> effects = new ArrayList<>(livingEntity.getActiveEffects());
			effects.sort(Comparator.comparing(effect -> {
				Identifier id = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
				return id != null ? id.toString() : "";
			}));
			return effects;
		}
		return Collections.emptyList();
	}

	public static ArrayList<String> execute(Entity entity) {
		if (entity == null)
			return new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
		for (MobEffectInstance effect : getSortedActiveEffects(entity)) {
			Identifier id = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
			list.add(id != null ? id.toString() : "");
		}
		return list;
	}
}