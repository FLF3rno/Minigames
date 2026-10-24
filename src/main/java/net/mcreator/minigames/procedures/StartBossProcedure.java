package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.FlavioFightManager;

public class StartBossProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).showBossBar = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if ((MinigamesModVariables.MapVariables.get(world).bossName).equals("Flavio")) {
			net.mcreator.minigames.FlavioFightManager.reset();
			SpawnSingleMachineProcedure.execute(world);
		}
	}
}