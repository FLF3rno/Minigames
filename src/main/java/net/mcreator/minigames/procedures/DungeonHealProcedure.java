package net.mcreator.minigames.procedures;

import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

public class DungeonHealProcedure {
	public static void execute(Entity entity, Entity target, double amount, String type) {
		if (entity == null || target == null || type == null)
			return;
		String Type = "";
		double Amount = 0;
		Entity Target = null;
		Target = target;
		Amount = amount;
		Type = type;
		if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.HEART_AMULET.get()))) {
			if ((Type).equals("passive")) {
				Amount = 0;
			} else {
				Amount = Amount + Amount * (GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entity, new ItemStack(MinigamesModItems.HEART_AMULET.get())), "minigames:effect_potency") / 100);
			}
		} else if (CheckRelicProcedure.execute(Target, new ItemStack(MinigamesModItems.HEART_AMULET.get()))) {
			if ((Type).equals("passive")) {
				Amount = 0;
			} else {
				Amount = Amount + Amount * (GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(Target, new ItemStack(MinigamesModItems.HEART_AMULET.get())), "minigames:effect_potency") / 100);
			}
		}
		if (Target instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.BLOCK_HEAL)) {
			Amount = Amount - Amount * (((target instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.BLOCK_HEAL) ? _livEnt.getEffect(MinigamesModMobEffects.BLOCK_HEAL).getAmplifier() : 0) + 1) / 100d);
		}
		if (target instanceof LivingEntity livingEntity && !target.level().isClientSide())
			livingEntity.heal((float) Amount);
	}
}