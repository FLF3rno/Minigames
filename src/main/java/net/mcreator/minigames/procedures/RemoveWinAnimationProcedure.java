package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class RemoveWinAnimationProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).winAnimationStart = false;
		MinigamesModVariables.MapVariables.get(world).hunteraWinAnimation = false;
		MinigamesModVariables.MapVariables.get(world).winAnimationTick = 0;
		MinigamesModVariables.MapVariables.get(world).winAnimationState = -1;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		MinigamesModVariables.winAnimation = -1;
	}
}
