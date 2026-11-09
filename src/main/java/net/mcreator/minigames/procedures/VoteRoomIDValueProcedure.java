package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class VoteRoomIDValueProcedure {
	public static void execute(LevelAccessor world, double ID) {
		MinigamesModVariables.MapVariables.get(world).voteRoomID = ID;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}