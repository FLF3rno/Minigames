package net.mcreator.minigames.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class RandomPotionProcedure {
	public static String execute() {
		double rng = 0;
		rng = Mth.nextInt(RandomSource.create(), 1, 8);
		if (rng == 1) {
			return "minigames:stunned";
		} else if (rng == 2) {
			return "minigames:blessed";
		} else if (rng == 3) {
			return "minigames:phantom";
		} else if (rng == 4) {
			return "minigames:damage_boost";
		} else if (rng == 5) {
			return "minigames:bleed";
		} else if (rng == 6) {
			return "minigames:decay";
		} else if (rng == 7) {
			return "minecraft:luck";
		} else if (rng == 8) {
			return "minecraft:unluck";
		}
		return "";
	}
}