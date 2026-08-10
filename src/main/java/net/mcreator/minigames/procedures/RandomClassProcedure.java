package net.mcreator.minigames.procedures;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class RandomClassProcedure {
	public static String execute() {
		String chosenClass = "";
		double rng = 0;
		rng = Mth.nextInt(RandomSource.create(), 1, 3);
		if (rng == 1) {
			chosenClass = "warrior";
		} else if (rng == 2) {
			chosenClass = "support";
		} else if (rng == 3) {
			chosenClass = "thief";
		}
		return chosenClass;
	}
}