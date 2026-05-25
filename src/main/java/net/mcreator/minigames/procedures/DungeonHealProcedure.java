package net.mcreator.minigames.procedures;

import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

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
		if (Target instanceof LivingEntity _entity)
			_entity.setHealth((float) ((target instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + Amount));
	}
}