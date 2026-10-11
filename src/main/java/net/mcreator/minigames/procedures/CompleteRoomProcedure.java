package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class CompleteRoomProcedure {
	public static void execute(LevelAccessor world) {
		double aliveEnemies = 0;
		MinigamesModVariables.MapVariables.get(world).inCombat = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		BreakDoorProcedure.execute(world, MinigamesModVariables.MapVariables.get(world).DoorPosition.x(), MinigamesModVariables.MapVariables.get(world).DoorPosition.y(), MinigamesModVariables.MapVariables.get(world).DoorPosition.z());
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			DungeonHealProcedure.execute(entityiterator, entityiterator, (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) - (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1), "room_complete");
			if (!(entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).gravity == Direction.DOWN)) {
				ChangeGravityExecuteProcedure.execute(Direction.DOWN, entityiterator);
			}
		}
		MinigamesModVariables.MapVariables.get(world).showBossBar = false;
		MinigamesModVariables.MapVariables.get(world).currentRoomType = "none";
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}