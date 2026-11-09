package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChangeCurrentRoomIDValueProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).currentRoomID = MinigamesModVariables.MapVariables.get(world).voteRoomID;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}