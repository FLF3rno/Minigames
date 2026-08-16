package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ChooseFloorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String floorTheme) {
		if (floorTheme == null)
			return;
		MinigamesModVariables.MapVariables.get(world).floorTypeDungeon = floorTheme;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		ChooseRandomBossProcedure.execute(world);
		if ((MinigamesModVariables.MapVariables.get(world).floorTypeDungeon).equals("church")) {
			MinigamesModVariables.MapVariables.get(world).dungeonRoomSize = new Vec3(29, 20, 29);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			SpawnGridProcedure.execute(world, x, y, z, 1, 13, 1, 9, 5, 5, 1);
		}
	}
}