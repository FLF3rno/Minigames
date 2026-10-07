package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChangeGravityExecuteProcedure {
	public static void execute(Direction gravity, Entity entity) {
		if (gravity == null || entity == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			Direction prevGravity = _vars.gravity;
			if (prevGravity == null) prevGravity = Direction.DOWN;
			if (prevGravity == gravity) return;

			net.minecraft.world.phys.Vec3 offset = net.mcreator.minigames.gravity.GravityOrientation.calculatePositionOffset(entity, prevGravity, gravity);

			_vars.gravity = gravity;
			_vars.markSyncDirty();

			double newX = entity.getX() + offset.x;
			double newY = entity.getY() + offset.y;
			double newZ = entity.getZ() + offset.z;

			if (entity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
				serverPlayer.teleportTo(newX, newY, newZ);
			} else {
				entity.setPos(newX, newY, newZ);
			}
			entity.refreshDimensions();
		}
	}
}