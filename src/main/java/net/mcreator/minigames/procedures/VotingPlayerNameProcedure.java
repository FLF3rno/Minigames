package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.UUID;

public class VotingPlayerNameProcedure {
	public static String execute(LevelAccessor world) {
		if (!((world instanceof ServerLevel _level0 ? getEntityFromUUID(_level0, MinigamesModVariables.MapVariables.get(world).VotingPlayerUUID) : null) == null)) {
			return MinigamesModVariables.MapVariables.get(world).VotingPlayerName;
		}
		return "null";
	}

	private static Entity getEntityFromUUID(ServerLevel level, String uuid) {
		try {
			return level.getEntity(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}