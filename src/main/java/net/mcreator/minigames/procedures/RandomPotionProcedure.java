package net.mcreator.minigames.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class RandomPotionProcedure {
	public static String execute() {
		double rng = 0;
		rng = Mth.nextInt(RandomSource.create(), 1, 14);
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
		} else if (rng == 9) {
			return "minigames:speed_boost";
		} else if (rng == 10) {
			return "minigames:damage_resistance";
		} else if (rng == 11) {
			return "minigames:golden";
		} else if (rng == 12) {
			return "minigames:slowdown";
		} else if (rng == 13) {
			return "minigames:lifesteal";
		} else if (rng == 14) {
			return "minigames:obsidianification";
		}
		return "";
	}
}