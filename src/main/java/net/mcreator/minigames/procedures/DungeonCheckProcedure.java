package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DungeonCheckProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double neighbour = 0;
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		boolean specialRoom = false;
		if (MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.x() <= MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.y()
				&& MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.z() >= MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.y() && MinigamesModVariables.MapVariables.get(world).dungeonSpawn == 1
				&& MinigamesModVariables.MapVariables.get(world).dungeonBoss == 1 && MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon == MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon
				&& MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon == MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon) {
			spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
			spawnRoomZ = MinigamesModVariables.MapVariables.get(world).dungeonSize.z();
			SpawnFloorProcedure.execute(world, x, y, z);
		} else {
			SpawnGridProcedure.execute(world, x, y, z, MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.z(),
					MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.x(), MinigamesModVariables.MapVariables.get(world).dungeonSize.x(),
					MinigamesModVariables.MapVariables.get(world).dungeonSize.z(), MinigamesModVariables.MapVariables.get(world).secretRoomDungeon);
		}
	}
}