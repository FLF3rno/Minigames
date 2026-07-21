package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class StartPVPProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).pvpAnimationStart = true;
		MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}