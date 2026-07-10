package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

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
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			prefix = "dungeon_four_";
			if ((MinigamesModVariables.MapVariables.get(world).floorTypeDungeon).equals("church")) {
				rng = Mth.nextInt(RandomSource.create(), 1, 2);
			}
			structureX = x;
			structureY = y;
			structureZ = z;
			spawnRoomName = prefix + "" + MinigamesModVariables.MapVariables.get(world).floorTypeDungeon + "_" + new java.text.DecimalFormat("##").format(rng);
			if (rng == 1) {
				structureY = structureY - 1;
			} else if (rng == 2) {
				structureY = structureY - 2;
			}
			rotation = "none";
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
		}
	}
}