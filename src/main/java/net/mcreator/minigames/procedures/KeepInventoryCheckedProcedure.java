package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

public class KeepInventoryCheckedProcedure {
	public static boolean execute(LevelAccessor world) {
		return world instanceof ServerLevel _serverLevelGR0 && _serverLevelGR0.getGameRules().get(GameRules.KEEP_INVENTORY);
	}
}