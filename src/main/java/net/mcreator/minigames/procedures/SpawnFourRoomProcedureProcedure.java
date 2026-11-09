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
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			rng = Mth.nextInt(RandomSource.create(), 1, (int) MinigamesModVariables.MapVariables.get(world).roguelike_four.size());
			structureX = x;
			structureY = y;
			structureZ = z;
			spawnRoomName = MinigamesModVariables.MapVariables.get(world).roguelike_four.get((int) (rng - 1)) instanceof String _str7 ? _str7 : "";
			if (MinigamesModVariables.MapVariables.get(world).roguelike_four.size() > 1) {
				MinigamesModVariables.MapVariables.get(world).roguelike_four.remove((int) (rng - 1));
			}
			if (("dungeon_four_church_1").equals(spawnRoomName)) {
				structureY = structureY - 1;
			} else if (("dungeon_four_church_2").equals(spawnRoomName)) {
				structureY = structureY - 21;
			} else if (("dungeon_four_church_4").equals(spawnRoomName)) {
				structureY = structureY - 18;
			} else if (("dungeon_four_church_6").equals(spawnRoomName)) {
				structureY = structureY - 1;
			}
			rotation = "none";
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
		}
	}
}