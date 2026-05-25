package net.mcreator.minigames.procedures;

import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.entity.Entity;

public class PotionEffectExpiresProcedure {
	public static void execute(Entity target, double level, String potion) {
		if (target == null || potion == null)
			return;
		String Potion = "";
		double Level = 0;
		Entity Target = null;
		Potion = potion;
		Target = target;
		Level = level - 1;
	}
}