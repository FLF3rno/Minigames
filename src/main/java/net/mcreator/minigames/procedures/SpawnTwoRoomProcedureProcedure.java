package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class SpawnTwoRoomProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		String spawnRoomName = "";
		String rotation = "";
		double structureX = 0;
		double structureZ = 0;
		double rng = 0;
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			rng = Mth.nextInt(RandomSource.create(), 1, 1);
			if (rng == 1) {
				spawnRoomName = "dungeon_two_church_seats";
			}
			rotation = "none";
			structureX = x;
			structureZ = z;
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp7 ? blockstate.setValue(_withbp7, true) : blockstate)) {
				structureX = x + 28;
				rotation = "clockwise_90";
			}
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, y, structureZ, rotation, spawnRoomName);
		}
	}
}