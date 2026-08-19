package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class BossbarshowProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).showBossBar = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}