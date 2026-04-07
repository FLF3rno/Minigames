package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class BreakAnimationProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).overlayAnimation1 = MinigamesModVariables.MapVariables.get(world).overlayAnimation1 + 1;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}