package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class IsNameNotWhiteProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color).equals("white")) {
			return true;
		}
		return false;
	}
}