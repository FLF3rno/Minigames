package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChangeCurrentRoomIDValueProcedure {
	public static void execute(LevelAccessor world, double ID) {
		MinigamesModVariables.MapVariables.get(world).currentRoomID = ID;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}