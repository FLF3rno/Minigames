package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class ChooseFloorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, double floor) {
		double floorTheme = 0;
		if (floor == 1) {
			floorTheme = Mth.nextInt(RandomSource.create(), 1, 1);
			if (floorTheme == 1) {
				MinigamesModVariables.MapVariables.get(world).floorTypeDungeon = "church";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
		ChooseRandomBossProcedure.execute(world);
		if ((MinigamesModVariables.MapVariables.get(world).floorTypeDungeon).equals("church")) {
			MinigamesModVariables.MapVariables.get(world).dungeonRoomSize = new Vec3(29, 20, 29);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			SpawnGridProcedure.execute(world, x, y, z, 1, 13, 1, 9, 5, 5, 1);
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.minimumLightLevel = 8;
					_vars.markSyncDirty();
				}
			}
		}
	}
}