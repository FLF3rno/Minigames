package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class SpawnFourRoomProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		double structureX = 0;
		double structureZ = 0;
		double rng = 0;
		double structureY = 0;
		String spawnRoomName = "";
		String rotation = "";
		String prefix = "";
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			prefix = "dungeon_four_church_";
			rng = Mth.nextInt(RandomSource.create(), 1, 2);
			structureX = x;
			structureY = y;
			structureZ = z;
			if (rng == 1) {
				spawnRoomName = prefix + "outside";
				structureY = structureY - 1;
			} else if (rng == 2) {
				spawnRoomName = prefix + "ritual";
				structureY = structureY - 2;
			}
			rotation = "none";
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
		}
	}
}