package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DungeonCoinsDisplayProcedure {
	public static String execute(LevelAccessor world) {
		double coins = MinigamesModVariables.MapVariables.get(world).dungeonCoins;
		return new java.text.DecimalFormat("###,###").format(coins);
	}
}
