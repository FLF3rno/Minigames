package net.mcreator.minigames.gravity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public final class GravityAccess {
	private GravityAccess() {}

	public static Direction getGravity(Entity entity) {
		if (entity == null) return Direction.DOWN;
		Direction direction = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity;
		return direction == null ? Direction.DOWN : direction;
	}
}
