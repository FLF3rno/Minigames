package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class AdrenalineBladeHandTickProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)) {
			ApplyEffectProcedure.execute(world, entity, false,
					GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") * Math.round(
							((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) / GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")),
					GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:damage_boost");
			ApplyEffectProcedure.execute(world, entity, false,
					GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") * Math.round(
							((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) / GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")),
					GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:speed_boost");
		}
	}
}