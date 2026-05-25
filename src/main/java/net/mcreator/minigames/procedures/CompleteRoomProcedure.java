package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class CompleteRoomProcedure {
	public static void execute(LevelAccessor world) {
		double aliveEnemies = 0;
		MinigamesModVariables.MapVariables.get(world).inCombat = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Room completed! (placeholder make an actual gui and shit)"), false);
		}
		BreakDoorProcedure.execute(world, MinigamesModVariables.MapVariables.get(world).DoorPosition.x(), MinigamesModVariables.MapVariables.get(world).DoorPosition.y(), MinigamesModVariables.MapVariables.get(world).DoorPosition.z());
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			DungeonHealProcedure.execute(entityiterator, (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) - (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1), "room_complete");
		}
	}
}