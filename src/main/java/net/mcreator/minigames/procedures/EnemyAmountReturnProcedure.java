package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class EnemyAmountReturnProcedure {
	public static String execute(LevelAccessor world) {
		return new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).aliveEnemies) + "/" + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).startingEnemies);
	}
}