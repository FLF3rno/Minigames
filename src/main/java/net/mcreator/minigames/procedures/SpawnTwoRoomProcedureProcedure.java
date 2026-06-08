package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpawnTwoRoomProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double z, BlockState blockstate) {
		double structureX = 0;
		double structureZ = 0;
		double rng = 0;
		double structureY = 0;
		String spawnRoomName = "";
		String rotation = "";
		String prefix = "";
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			prefix = "dungeon_two_church_";
			rng = Mth.nextInt(RandomSource.create(), 1, 1);
			structureX = x;
			structureY = x;
			structureZ = z;
			if (rng == 1) {
				spawnRoomName = prefix + "dungeon_two_church_seats";
			}
			rotation = "none";
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp7 ? blockstate.setValue(_withbp7, true) : blockstate)) {
				structureX = structureX + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() - 1;
				rotation = "clockwise_90";
			}
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
		}
	}
}